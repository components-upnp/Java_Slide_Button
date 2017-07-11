package com.irit.upnp;

import com.irit.main.DefilementFrame;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import java.io.IOException;

/**
 * Created by IDA on 02/02/2017.
 */
public class DefilementServer implements Runnable {

    /**
     * Main
     * Copy code if you need to add a Upnp service on your device
     * @param args
     * @throws Exception
     */

    private DefilementFrame frameC;
    private UDN udn;

    /**
     * Run the UPnP service
     */
    public void run() {
        try {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });

            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    createDevice()
            );

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * Permet de crï¿½er un device
     * Il est possible de crï¿½er plusieurs service pour un mï¿½me device, dans ce cas confer commentaires en fin de methode.
     * @return LocalDevice
     * @throws ValidationException
     * @throws LocalServiceBindingException
     * @throws IOException
     */
    public LocalDevice createDevice()
            throws ValidationException, LocalServiceBindingException, IOException {

        /**
         * Description du Device
         */

        udn = UDN.uniqueSystemIdentifier("DefilementService");

        DeviceIdentity identity =
                new DeviceIdentity(
                        udn
                );

        DeviceType type =
                new UDADeviceType("TypeCompteur", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Friendly DefilementService",					// Friendly Name
                        new ManufacturerDetails(
                                "CreaTech",								// Manufacturer
                                ""),								// Manufacturer URL
                        new ModelDetails(
                                "DefilementTest",						// Model Name
                                "un composant qui permet de faire defiler sur la gauche ou la droite.",	// Model Description
                                "v1" 								// Model Number
                        )
                );


        // Ajout du service


        LocalService<DefilementService> defilementService =
                new AnnotationLocalServiceBinder().read(DefilementService.class);

        defilementService.setManager(
                new DefaultServiceManager(defilementService, DefilementService.class)
        );


        new DefilementFrame(defilementService, udn).setVisible(true);


        // retour en cas de 1 service
        return new LocalDevice(identity, type, details, defilementService);


		/* Si jamais plusieurs services pour un device (adapter code)
	    return new LocalDevice(
	            identity, type, details,
	            new LocalService[] {switchPowerService, myOtherService}
	    );
	    */
    }


}
