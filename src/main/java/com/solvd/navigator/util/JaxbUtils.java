package com.solvd.airport.util;

import com.solvd.airport.domain.Gate;
import com.solvd.airport.domain.GateCollection;
import com.solvd.airport.exception.UnmarshallingFailureException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;


public class JaxbUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.JAXB_UTILS);

    public static Gate getGateByCodes(String gateCode, String airportCode) {
        return getGateByCodes(FilepathConstants.GATES_XML, gateCode, airportCode);
    }

    public static Gate getGateByCodes(String filepath, String gateCode, String airportCode) {
        GateCollection gateCollection = extractGates(filepath);

        return gateCollection.getGates().stream()
                .filter(gate ->
                        airportCode.equals(gate.getAirportCode())
                                && gateCode.equals(gate.getGateCode()))
                .findFirst()
                .orElse(null);
    }

    public static GateCollection extractGates() {
        return extractGates(FilepathConstants.GATES_XML);
    }

    public static GateCollection extractGates(String filepath) {
        return extractFromFile(filepath, ClassConstants.GATE_COLLECTION);
    }

    public static <T> T extractFromFile(String filepath, Class<T> clazz) {
        try {
            InputStream inputStream =
                    ClassConstants.JAXB_UTILS
                            .getClassLoader()
                            .getResourceAsStream(filepath);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filepath);
            }

            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return clazz.cast(unmarshaller.unmarshal(inputStream));
        } catch (JAXBException e) {
            throw new UnmarshallingFailureException("Error unmarshalling file: " + filepath + e);
        }
    }

    private JaxbUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }


}
