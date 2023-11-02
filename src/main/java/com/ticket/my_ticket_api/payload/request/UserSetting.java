package com.ticket.my_ticket_api.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSetting {
    private String firstName;
    private String lastName;
    private String phone;
    private String website;
    private String Company;
    private String position;
    private String address;
    private String address2;
    private String city;
    private String country;
    private String country_code;
    private String code_postal;
    private String state;
    private String state_code;
}
