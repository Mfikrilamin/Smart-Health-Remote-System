package com.SmartHealthRemoteSystem.SHSR.SendDailyHealth;

import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/Health-status")
public class SendHealthStatusController {
    private final HealthStatusService healthStatusService;

    public SendHealthStatusController(HealthStatusService healthStatusService) {
        this.healthStatusService = healthStatusService;
    }

    @PostMapping("/create-healthstatus/{patientId}")
    public void saveHealthStatus(@RequestBody HealthStatus healthStatus, @PathVariable String patientId)
            throws ExecutionException, InterruptedException {
        healthStatusService.createHealthStatus(healthStatus, patientId);
    }

    @GetMapping("/get-healthstatus/{patientId}/{healthStatusId}")
    public HealthStatus getHealthStatus(@PathVariable String healthStatusId, @PathVariable String patientId) throws ExecutionException, InterruptedException {

        return healthStatusService.getHealthStatus(healthStatusId, patientId);
    }

    @PutMapping("/update-healthstatus/{patientId}")
    public void updateHealthStatus(@RequestBody HealthStatus healthStatus, @PathVariable String patientId) throws ExecutionException, InterruptedException {
        healthStatusService.updateHealthStatus(healthStatus, patientId);
    }

    @DeleteMapping("/delete-healthstatus/{patientId}/{healthStatusId}")
    public void deleteHealthStatus(@PathVariable String healthStatusId, @PathVariable String patientId) throws ExecutionException, InterruptedException {
        healthStatusService.deleteHealthStatus(healthStatusId, patientId);
    }

    @PostMapping("/sendHealthStatus")
    public String sendHealthStatus(@RequestParam(value = "symptom") String symptom, @RequestParam(value="patientID") String patientID, @RequestParam (value = "doctorID")String doctorID) throws ExecutionException, InterruptedException {

        HealthStatus healthStatus=new HealthStatus(symptom,doctorID);
        healthStatusService.createHealthStatus(healthStatus,patientID);

        return "patientDashBoard";
    }

    @PostMapping("/viewHealthStatusForm")
    public String healthStatusForm(@RequestParam (value = "patientID")String patientID, @RequestParam(value="doctorID")String doctorID, Model model) throws ExecutionException, InterruptedException {
//        List<HealthStatus>  healthStatus=healthStatusService.getListHealthStatus(patientID);
//        HealthStatus firstHealthStatus=healthStatus.get(0);



        model.addAttribute("patientID", patientID);
        model.addAttribute("doctorID", doctorID);

        return "sendDailyHealthSymptom";

    }

}




