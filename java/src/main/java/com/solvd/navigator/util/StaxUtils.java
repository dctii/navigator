package com.solvd.airport.util;

import com.solvd.airport.domain.AirlineStaffMember;
import com.solvd.airport.domain.EmailAddress;
import com.solvd.airport.domain.PersonInfo;
import com.solvd.airport.domain.Terminal;
import com.solvd.airport.exception.ReadXmlWithStaxFailureException;
import com.solvd.airport.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StaxUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.STAX_UTILS);


    public static AirlineStaffMember getAirlineStaffMemberByEmail(String email) {
        return getAirlineStaffMemberByEmail(
                FilepathConstants.AIRLINE_STAFF_XML,
                email
        );
    }

    public static AirlineStaffMember getAirlineStaffMemberByEmail(String filepath, String email) {
        List<AirlineStaffMember> airlineStaffMembers = extractAirlineStaff(filepath);

        return airlineStaffMembers.stream()
                .filter(airlineStaffMember -> airlineStaffMember.getPersonInfo() != null)
                .filter(airlineStaffMember -> airlineStaffMember.getEmailAddressByEmailAddress(email) != null)
                .findFirst()
                .orElse(null);
    }


    public static List<AirlineStaffMember> extractAirlineStaff(String filepath) {
        List<AirlineStaffMember> airlineStaff = new ArrayList<>();

        try (
                InputStream inputStream = ClassConstants.STAX_UTILS.getClassLoader().getResourceAsStream(filepath)
        ) {
            if (inputStream == null) {
                throw new ResourceNotFoundException("Resource not found: " + filepath);
            }

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            AirlineStaffMember airlineStaffMember = null;
            PersonInfo personInfo = null;
            EmailAddress emailAddress = null;
            Set<EmailAddress> emailAddresses = null;

            String elementName = StringConstants.EMPTY_STRING;

            final String AIRLINE_STAFF_MEMBER_ELEMENT_STRING = "airlineStaffMember";
            final String ID_ATTRIBUTE_STRING = "id";
            final String MEMBER_ROLE_FIELD_STRING = "memberRole";

            final String PERSON_INFO_ELEMENT_STRING = "personInfo";
            final String SURNAME_FIELD_STRING = "surname";
            final String GIVEN_NAME_FIELD_STRING = "givenName";
            final String MIDDLE_NAME_FIELD_STRING = "middleName";
            final String BIRTHDATE_FIELD_STRING = "birthdate";
            final String SEX_FIELD_STRING = "sex";

            final String EMAIL_ADDRESSES_ELEMENT_STRING = "emailAddresses";
            final String EMAIL_ADDRESS_ELEMENT_STRING = "emailAddress";
            final String EMAIL_ADDRESS_FIELD_STRING = "emailAddress";


            while (reader.hasNext()) {
                int eventType = reader.next();

                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                        elementName = reader.getLocalName();
                        switch (elementName) {
                            case AIRLINE_STAFF_MEMBER_ELEMENT_STRING:
                                airlineStaffMember = new AirlineStaffMember();
                                airlineStaffMember.setAirlineStaffId(
                                        parseUnsignedIntAttribute(
                                                reader,
                                                ID_ATTRIBUTE_STRING
                                        ));
                                break;
                            case PERSON_INFO_ELEMENT_STRING:
                                personInfo = new PersonInfo();
                                personInfo.setPersonInfoId(
                                        parseUnsignedIntAttribute(
                                                reader,
                                                ID_ATTRIBUTE_STRING
                                        ));

                                emailAddresses = new HashSet<>();
                                break;
                            case EMAIL_ADDRESS_ELEMENT_STRING:
                                emailAddress = new EmailAddress();
                                emailAddress.setEmailAddressId(
                                        parseUnsignedIntAttribute(
                                                reader,
                                                ID_ATTRIBUTE_STRING
                                        ));
                                break;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        String text = reader.getText().trim();
                        if (!text.isEmpty()) {
                            switch (elementName) {
                                case MEMBER_ROLE_FIELD_STRING:
                                    if (airlineStaffMember != null) {
                                        airlineStaffMember.setMemberRole(text);
                                    }
                                    break;
                                case SURNAME_FIELD_STRING:
                                    if (personInfo != null) {
                                        personInfo.setSurname(text);
                                    }
                                    break;
                                case GIVEN_NAME_FIELD_STRING:
                                    if (personInfo != null) {
                                        personInfo.setGivenName(text);
                                    }
                                    break;
                                case MIDDLE_NAME_FIELD_STRING:
                                    if (personInfo != null) {
                                        personInfo.setMiddleName(text);
                                    }
                                    break;
                                case BIRTHDATE_FIELD_STRING:
                                    if (personInfo != null) {
                                        personInfo.setBirthdate(text);
                                    }
                                    break;
                                case SEX_FIELD_STRING:
                                    if (personInfo != null) {
                                        personInfo.setSex(text);
                                    }
                                    break;
                                case EMAIL_ADDRESS_FIELD_STRING:
                                    if (emailAddress != null) {
                                        emailAddress.setEmailAddress(text);
                                    }
                                    break;
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        elementName = reader.getLocalName();
                        if (elementName.equals(EMAIL_ADDRESSES_ELEMENT_STRING) && emailAddress != null && emailAddresses != null) {
                            emailAddresses.add(emailAddress);
                            emailAddress = null;
                        } else if (elementName.equals(PERSON_INFO_ELEMENT_STRING) && personInfo != null) {
                            personInfo.setEmailAddresses(emailAddresses);
                            emailAddresses = null;
                        } else if (elementName.equals(AIRLINE_STAFF_MEMBER_ELEMENT_STRING) && airlineStaffMember != null) {
                            airlineStaffMember.setPersonInfo(personInfo);
                            personInfo = null;
                            airlineStaff.add(airlineStaffMember);
                            airlineStaffMember = null;
                        }
                        break;
                }
            }

        } catch (XMLStreamException | IOException e) {
            throw new ReadXmlWithStaxFailureException("Failure reading XML with StAX:" + e);
        }

        return airlineStaff;
    }

    public static Terminal getTerminalByCodes(String airportCode, String terminalCode) {
        List<Terminal> terminals = extractTerminals();
        return terminals.stream()
                .filter(terminal ->
                        airportCode.equals(terminal.getAirportCode())
                                && terminalCode.equals(terminal.getTerminalCode()))
                .findFirst()
                .orElse(null);
    }

    public static Terminal getTerminalByCodes(String filepath, String airportCode, String terminalCode) {
        List<Terminal> terminals = extractTerminals(filepath);
        return terminals.stream()
                .filter(terminal ->
                        airportCode.equals(terminal.getAirportCode())
                                && terminalCode.equals(terminal.getTerminalCode()))
                .findFirst()
                .orElse(null);
    }

    public static Terminal getTerminalByAirportAndName(String airportCode, String terminalName) {
        List<Terminal> terminals = extractTerminals();
        return terminals.stream()
                .filter(terminal -> airportCode.equals(terminal.getAirportCode())
                        && terminalName.equals(terminal.getTerminalName()))
                .findFirst()
                .orElse(null);
    }

    public static Terminal getTerminalByAirportAndName(String filepath, String airportCode, String terminalName) {
        List<Terminal> terminals = extractTerminals(filepath);
        return terminals.stream()
                .filter(terminal ->
                        airportCode.equals(terminal.getAirportCode())
                                && terminalName.equals(terminal.getTerminalName()))
                .findFirst()
                .orElse(null);
    }

    public static List<Terminal> extractTerminals() {
        return extractTerminals(FilepathConstants.TERMINALS_XML);
    }

    public static List<Terminal> extractTerminals(String filepath) {
        List<Terminal> terminals = new ArrayList<>();

        try (
                InputStream inputStream = ClassConstants.STAX_UTILS.getClassLoader().getResourceAsStream(filepath)
        ) {
            if (inputStream == null) {
                throw new ResourceNotFoundException("Resource not found: " + filepath);
            }

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            Terminal terminal = null;
            String elementName = StringConstants.EMPTY_STRING;

            final String elementNameString = "terminal";
            final String airportCodeFieldString = "airportCode";
            final String terminalCodeFieldString = "terminalCode";
            final String terminalNameFieldString = "terminalName";
            final String isInternationalFieldString = "isInternational";
            final String isDomesticFieldString = "isDomestic";

            while (reader.hasNext()) {
                int eventType = reader.next();

                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                        elementName = reader.getLocalName();
                        if (elementNameString.equals(elementName)) {
                            terminal = new Terminal();
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        String text = reader.getText().trim();
                        if (!text.isEmpty()) {
                            switch (elementName) {
                                case airportCodeFieldString:
                                    terminal.setAirportCode(text);
                                    break;
                                case terminalCodeFieldString:
                                    terminal.setTerminalCode(text);
                                    break;
                                case terminalNameFieldString:
                                    terminal.setTerminalName(text);
                                    break;
                                case isInternationalFieldString:
                                    terminal.setInternational(Boolean.parseBoolean(text));
                                    break;
                                case isDomesticFieldString:
                                    terminal.setDomestic(Boolean.parseBoolean(text));
                                    break;
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (elementNameString.equals(reader.getLocalName())) {
                            terminals.add(terminal);
                        }
                        break;
                }
            }

        } catch (XMLStreamException | IOException e) {
            throw new ReadXmlWithStaxFailureException("Failure reading XML with StAX: " + e);
        }

        return terminals;
    }

    private static int parseUnsignedIntAttribute(XMLStreamReader reader, String attributeName) {
        String value = reader.getAttributeValue(
                null,
                attributeName
        );
        return (value != null)
                ? Integer.parseInt(value)
                : -1;
    }

    private StaxUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
