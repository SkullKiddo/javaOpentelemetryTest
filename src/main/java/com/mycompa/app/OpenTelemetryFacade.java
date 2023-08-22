package com.mycompa.app;

import java.time.Duration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;


public class OpenTelemetryFacade {

    private static LongCounter counter;
    private static ObservableDoubleGauge gauge;
    private static OpenTelemetry openTelemetry;
    private static String ENDPOINT = "http://127.0.0.1:4317";
    private static Duration INTERVAL_DURATION = Duration.ofMillis(1000);
    private static double innerCounter = 0.5;
    // It is recommended that the API user keep a reference to Attributes they will record against
    private static Attributes attributes = Attributes.of(AttributeKey.stringKey("Key"), "SomeWork");

    private static void obtainOpentelemetry(){
        Resource resource = Resource.getDefault()
        .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name")));

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
            .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().setEndpoint(ENDPOINT).build()).setInterval(INTERVAL_DURATION).build())
            .setResource(resource)
            .build();

        
        OpenTelemetryFacade.openTelemetry = OpenTelemetrySdk.builder()
            .setMeterProvider(sdkMeterProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal();
    }


    private static void obtainCounter(){
        // Gets or creates a named meter instance
        Meter meter = openTelemetry.meterBuilder("instrumentation_scope_name")
                .setInstrumentationVersion("1.0.0")
                .build();

        // Build counter e.g. LongCounter
        OpenTelemetryFacade.counter = meter
            .counterBuilder("meter_name")
            .setDescription("meter_description")
            .setUnit("1")
            .build();
    }

    public static void obtainGauge(){

        // Gets or creates a named meter instance
        Meter meter = openTelemetry.meterBuilder("instrumentation_scope_name")
                .setInstrumentationVersion("1.0.0")
                .build();

        // Build counter e.g. LongCounter
        OpenTelemetryFacade.gauge = meter
            .gaugeBuilder("meter_name")
            .setDescription("meter_description")
            .setUnit("1")
            .buildWithCallback((mesurement -> mesurement.record(innerCounter, OpenTelemetryFacade.attributes)));
    }

    public static void setupOpentelemetry() {
        obtainOpentelemetry();
        obtainCounter();
        obtainGauge();
    }


    public static void updateMetrics(){
        System.out.println("Incrementing the counter");
        innerCounter++;
        OpenTelemetryFacade.counter.add(1, OpenTelemetryFacade.attributes);
    }
}
