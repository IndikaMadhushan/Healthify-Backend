package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorRegisterRequest;
import com.healthcare.personal_health_monitoring.dto.PatientRegisterRequest;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.PendingRegistration;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.PendingRegistrationRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.security.JWTUtil;
import com.healthcare.personal_health_monitoring.service.AuthService;
import com.healthcare.personal_health_monitoring.service.EmailService;
import com.healthcare.personal_health_monitoring.service.EmailValidationService;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import com.healthcare.personal_health_monitoring.service.IdGeneratorService;
import com.healthcare.personal_health_monitoring.util.AgeUtil;
import com.healthcare.personal_health_monitoring.util.OtpGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

//Note: User here is the generic user entity
// (not Patient/Doctor typed). If you use subclassed
// entities and want to persist Patient/Doctor records
// separately, modify register method to create Patient
// or Doctor object and save via appropriate repository.
// This version keeps it simple and safe.


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final IdGeneratorService idGeneratorService;
    private final FileUploadService fileUploadService;
    private final EmailValidationService emailValidationService;
    private final EmailService emailService;



    /**
     Registers a new user.
        PATIENT → enabled immediately
        DOCTOR  → disabled until admin approval
        ADMIN   → already in the system
     */
    @Override
    @Transactional
    public void registerPatient(PatientRegisterRequest req)  {
        // Prevent duplicate emails in users (allow retry if email not verified)
        if (refreshOtpForUnverifiedUser(req.getEmail(), UserRole.PATIENT, req.getPassword())) {
            return;
        }

        PendingRegistration existing = pendingRegistrationRepository.findByEmail(req.getEmail()).orElse(null);
        if (existing != null && existing.getRole() != UserRole.PATIENT) {
            throw new IllegalArgumentException("Email is already registered for a different account type.");
        }

        //to prevent nic duplicate

        if(patientRepository.findByNic(req.getNic()).isPresent()
                || (existing == null && pendingRegistrationRepository.existsByNic(req.getNic()))
                || (existing != null && pendingRegistrationRepository.existsByNicAndEmailNot(req.getNic(), req.getEmail()))){
            throw new IllegalArgumentException("NIC already registerd as a patient");
        }

        //OTP generation part
        String otp = OtpGenerator.generateOtp();
        PendingRegistration pending = (existing != null) ? existing : new PendingRegistration();
        pending.setEmail(req.getEmail());
        pending.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        pending.setRole(UserRole.PATIENT);
        pending.setFullName(req.getFullName());
        pending.setDateOfBirth(req.getDateOfBirth());
        pending.setPhone(req.getPhone());
        pending.setGender(req.getGender());
        pending.setNic(req.getNic());
        pending.setEmailOtp(otp);
        pending.setOtpGeneratedAt(LocalDateTime.now());

        if(!emailValidationService.isValidEmail(req.getEmail())){
            throw new RuntimeException("Email does ot exist or invalid");
        }



        pendingRegistrationRepository.save(pending);

        emailService.sendOtpEmail(req.getEmail(), otp);

    }


    @Transactional
    public void registerDoctor(
            DoctorRegisterRequest req,
            MultipartFile verificationDoc
    ){
        // to Prevent duplicate emails (allow retry if email not verified)
        if (refreshOtpForUnverifiedUser(req.getEmail(), UserRole.DOCTOR, req.getPassword())) {
            return;
        }

        PendingRegistration existing = pendingRegistrationRepository.findByEmail(req.getEmail()).orElse(null);
        if (existing != null && existing.getRole() != UserRole.DOCTOR) {
            throw new IllegalArgumentException("Email is already registered for a different account type.");
        }
        // prevent duplicate nic
        if (doctorRepository.findByNic(req.getNic()).isPresent()
                || (existing == null && pendingRegistrationRepository.existsByNic(req.getNic()))
                || (existing != null && pendingRegistrationRepository.existsByNicAndEmailNot(req.getNic(), req.getEmail()))) {
            throw new IllegalArgumentException("NIC already registered as a doctor.");
        }

        // prevent duplicate Licence number
        if (doctorRepository.findByLicenseNumber(req.getLicenseNumber()).isPresent()
                || (existing == null && pendingRegistrationRepository.existsByLicenseNumber(req.getLicenseNumber()))
                || (existing != null && pendingRegistrationRepository.existsByLicenseNumberAndEmailNot(req.getLicenseNumber(), req.getEmail()))) {
            throw new IllegalArgumentException("Licence is already registered as a doctor.");
        }

        //upload verification document
        String docPath = fileUploadService.uploadPrivateFile(
            verificationDoc,
            "doctor-verification-docs",
            "pending/" + req.getEmail()
        );

        if(!emailValidationService.isValidEmail(req.getEmail())){
            throw new RuntimeException("Email does ot exist or invalid");
        }

        //OTP generqtion part
        String otp = OtpGenerator.generateOtp();

        PendingRegistration pending = (existing != null) ? existing : new PendingRegistration();
        pending.setEmail(req.getEmail());
        pending.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        pending.setRole(UserRole.DOCTOR);
        pending.setFullName(req.getFullName());
        pending.setGender(req.getGender());
        pending.setSpecialization(req.getSpecialization());
        pending.setHospital(req.getHospital());
        pending.setNic(req.getNic());
        pending.setLicenseNumber(req.getLicenseNumber());
        pending.setDateOfBirth(req.getDateOfBirth());
        pending.setEmailOtp(otp);
        pending.setOtpGeneratedAt(LocalDateTime.now());
        pending.setVerificationDocUrl(docPath);

        pendingRegistrationRepository.save(pending);

        emailService.sendOtpEmail(req.getEmail(), otp);
    }


    /**
     * Login user and generate JWT token.
     */
    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Wrong email or password"
                ));

        // Password verification
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Wrong email or password"
            );
        }

        //block until the email validation
        if(!user.isEmailVerified()){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Please verify your email before log in"
            );
        }

        // Block disabled accounts
        if (!user.isEnabled()) {
            if (user.getRole() == UserRole.DOCTOR) {
                throw new SecurityException("Doctor account not approved yet. Please wait for admin approval.");
            }
            throw new SecurityException("Account not enabled. Contact admin.");
        }



        // Generate JWT
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    @Transactional
    public String verifyEmailOtp(String email, String otp) {
        // Prefer pending registration flow
        PendingRegistration pending = pendingRegistrationRepository.findByEmail(email).orElse(null);
        if (pending != null) {
            if (!pending.getEmailOtp().equals(otp)) {
                throw new IllegalArgumentException("Invalid otp");
            }

            // OTP expiry check (5 minutes)
            LocalDateTime expiryTime = pending.getOtpGeneratedAt().plusMinutes(5);
            if (LocalDateTime.now().isAfter(expiryTime)) {
                pendingRegistrationRepository.delete(pending);
                throw new IllegalArgumentException("OTP expired. Please register again.");
            }

            User user = new User();
            user.setEmail(pending.getEmail());
            user.setPassword(pending.getPasswordHash());
            user.setRole(pending.getRole());
            user.setEnabled(pending.getRole() == UserRole.PATIENT);
            user.setEmailVerified(true);
            user.setEmailOtp(null);
            user.setOtpGeneratedAt(LocalDateTime.now());

            userRepository.save(user);

            if (pending.getRole() == UserRole.PATIENT) {
                Patient patient = new Patient();
                patient.setUser(user);
                patient.setFullName(pending.getFullName());
                patient.setDateOfBirth(pending.getDateOfBirth());
                patient.setAge(AgeUtil.calculateAge(pending.getDateOfBirth()));
                patient.setPhone(pending.getPhone());
                patient.setGender(pending.getGender());
                patient.setNic(pending.getNic());
                patient.setPatientId(idGeneratorService.generatePatientCode());
                patient.setUpdatedAt(LocalDateTime.now());
                patientRepository.save(patient);
            } else if (pending.getRole() == UserRole.DOCTOR) {
                Doctor doctor = new Doctor();
                doctor.setUser(user);
                doctor.setFullName(pending.getFullName());
                doctor.setGender(pending.getGender());
                doctor.setLegacyGender(pending.getGender());
                doctor.setSpecialization(pending.getSpecialization());
                doctor.setHospital(pending.getHospital());
                doctor.setLegacyHospital(pending.getHospital());
                doctor.setNic(pending.getNic());
                doctor.setLicenseNumber(pending.getLicenseNumber());
                doctor.setLegacyLicenseNumber(pending.getLicenseNumber());
                doctor.setDateOfBirth(pending.getDateOfBirth());
                doctor.setAge(AgeUtil.calculateAge(pending.getDateOfBirth()));
                doctor.setVerificationDocUrl(pending.getVerificationDocUrl());
                doctor.setDoctorId(idGeneratorService.generateDoctorCode());
                doctorRepository.save(doctor);
            }

            pendingRegistrationRepository.delete(pending);
            return "Email Verified Successfully";
        }

        // Fallback for existing users (legacy flow)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailOtp() == null) {
            throw new IllegalArgumentException("OTP expired or already used");
        }

        if (user.getEmailOtp().equals(otp)) {
            user.setEmailVerified(true);
            user.setEmailOtp(null);
            if (user.getOtpGeneratedAt() == null) {
                user.setOtpGeneratedAt(LocalDateTime.now());
            }
            userRepository.save(user);
            return "Email Verified Successfully";
        }

        throw new IllegalArgumentException("Invalid otp");
    }

    @Override
    @Transactional
    public String resendEmailOtp(String email) {
        PendingRegistration pending = pendingRegistrationRepository.findByEmail(email).orElse(null);
        if (pending != null) {
            String otp = OtpGenerator.generateOtp();
            pending.setEmailOtp(otp);
            pending.setOtpGeneratedAt(LocalDateTime.now());
            pendingRegistrationRepository.save(pending);
            emailService.sendOtpEmail(email, otp);
            return "OTP resent successfully";
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        String otp = OtpGenerator.generateOtp();
        user.setEmailOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendOtpEmail(email, otp);
        return "OTP resent successfully";
    }


    /**
     * Admin approves a doctor account
     */
    public void approveDoctor(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.DOCTOR) {
            throw new RuntimeException("Only doctor accounts can be approved");
        }

        user.setEnabled(true); // Approve doctor
        userRepository.save(user);
    }

    public List<User> getPendingDoctors() {

        return userRepository.findByRoleAndEnabled(UserRole.DOCTOR, false);
    }

    // Admin rejects a doctor registration

    @Transactional
    public void rejectDoctor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.DOCTOR) {
            throw new RuntimeException("Only doctor accounts can be rejected");
        }

        // Find associated doctor record
        Doctor doctor = doctorRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Doctor record not found"));

        // Delete doctor and user records
        doctorRepository.delete(doctor);
        userRepository.delete(user);

        // Optional: Send rejection email
        emailService.sendRejectionEmail(user.getEmail(), doctor.getFullName());
    }

    private boolean refreshOtpForUnverifiedUser(String email, UserRole role, String rawPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email already in use. Use another Email.");
        }

        if (user.getRole() != role) {
            throw new IllegalArgumentException("Email is already registered for a different account type.");
        }

        String otp = OtpGenerator.generateOtp();
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmailOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        pendingRegistrationRepository.findByEmail(email)
                .ifPresent(pendingRegistrationRepository::delete);

        emailService.sendOtpEmail(email, otp);
        return true;
    }

}
