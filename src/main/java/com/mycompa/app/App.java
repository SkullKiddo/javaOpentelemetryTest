package com.mycompa.app;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println("Starting whatever");
        OpenTelemetryFacade.setupOpentelemetry();
        while(true){
            System.out.println("Calling update");
            OpenTelemetryFacade.updateMetrics();
            System.out.println("Sleeping 1s");
            Thread.sleep(1000);
        }
        // System.out.println("Ending whatever");
    }
}




// otlr -ex> opentlmtrcollector ipipipip:port

