/*
 MediTicket02Application.java

 Main class that starts the MediTicket backend

 Authors:
  Joshua Reid Adams (230317693)
  Abdullahi Raage Farah (230971091)
  Aidan Barends (230255639)
  Jaden Clayton Abrahams (222206721)
  Joshua Peter Bonzet (221312536)
  Matthew Barron (230398863)
  Raul Jaaim Everts (230270565)

 Date: 20th June 2026
*/

package za.ac.cput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // background notification emails (EmailService.sendNotificationEmail)
public class MediTicket02Application {

    public static void main(String[] args) {
        SpringApplication.run(MediTicket02Application.class, args);
    }

}
