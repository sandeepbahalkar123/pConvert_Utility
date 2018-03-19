/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.oo_to_pdf_conversion;

import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import ooo.connector.BootstrapSocketConnector;

/**
 *
 * @author riteshpandhurkar
 */
public class ConfigureOOXDesktop {

    private static ConfigureOOXDesktop instance = null;
    private static XDesktop xDesktop = null;
    private static XComponentLoader xComponentLoader = null;

    protected ConfigureOOXDesktop(String ooLibPath) {
        try {

            // Initialise OO
            XComponentContext xContext = BootstrapSocketConnector.bootstrap(ooLibPath);

            XMultiComponentFactory xMCF = xContext.getServiceManager();

            Object oDesktop = xMCF.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);

            xDesktop = (com.sun.star.frame.XDesktop) UnoRuntime.queryInterface(
                    com.sun.star.frame.XDesktop.class, oDesktop);

            xComponentLoader = (XComponentLoader) UnoRuntime
                    .queryInterface(com.sun.star.frame.XComponentLoader.class, xDesktop);

        } catch (BootstrapException | com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        }
    }

    // Lazy Initialization (If required then only)
    public static ConfigureOOXDesktop getInstance(String ooLibPath) {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (ConfigureOOXDesktop.class) {
                if (instance == null) {
                    instance = new ConfigureOOXDesktop(ooLibPath);
                }
            }
        }
        return instance;
    }

    public static XComponentLoader getXComponentLoader(String ooLibPath) {
        getInstance(ooLibPath);
        return xComponentLoader;
    }

    public static void stopXDesktop() {
        if (xComponentLoader != null) {
            System.out.println("Shutting down xDesktop...");
            xDesktop.terminate();
            //TODO: This is manual exists, need to find to close programme.
            System.exit(-1);
        }
    }

}
