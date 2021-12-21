package com.SmartHealthRemoteSystem.SHSR.SendPrescriptions;

import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientService;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.PrescriptionService;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/prescription")
public class SendPrescriptionController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    @Autowired
    public SendPrescriptionController(DoctorService doctorService, PatientService patientService, PrescriptionService prescriptionService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/form")
    public String getPrescriptionForm(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        return "sendPrescriptionForm";
    }

//    @RequestMapping(value = "/form/submit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/form/submit")
    public String submitPrescriptionForm(@ModelAttribute PrescriptionFormDTO prescriptionFormDTO, Model model)
//                                             @RequestParam(value = "patientId") String patientId,
//                                         @RequestParam(value = "doctorId") String doctorId,
//                                         @RequestParam(value = "prescription") String prescription,
//                                         @RequestParam(value = "diagnosisAilment") String diagnosisAilment,
//                                         @RequestParam(value = "medicine") List<String> medicineList)
            throws ExecutionException, InterruptedException {
        Prescription prescription1 = new Prescription(prescriptionFormDTO.getDoctorId(),
                prescriptionFormDTO.getMedicine(),
                prescriptionFormDTO.getPrescription(),
                prescriptionFormDTO.getDiagnosisAilment());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        model.addAttribute("doctor",doctor);
        String timeCreated = prescriptionService.createPrescription(prescription1,prescriptionFormDTO.getPatientId());
        //Will update more later. Need to figure to which page after processing the form?
        return "myPatient";
    }

}
