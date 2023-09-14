Simple concept test to try manual instrumentation on java with the opentelemetry collector running on a container.

In order to launch use it, launch the scripts: launch_agent.sh and launch_collector.sh

launch_agent.sh compiles and executes App.java which along with OpenTelemetryFacade.java configures and instanciates a simple code instrumented by opentelemetry to be collected by the collector (the endpoint of this collector and the time between updates to the collector can be configured in the variables of the facade ENDPOINT and INTERVAL_DURATION).

launch_collector.sh starts a docker container with the OpenTelemetry collector and automatically receives and print in the console the values received.
