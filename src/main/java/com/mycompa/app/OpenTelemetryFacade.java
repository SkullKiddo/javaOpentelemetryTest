package com.mycompa.app;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;


public class OpenTelemetryFacade {

    private static LongCounter counter;
    private static int inner_counter = 0;
    private static OpenTelemetry openTelemetry;

    private static void obtainOpentelemetry(){
        Resource resource = Resource.getDefault()
        .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name")));

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
            .setResource(resource)
            .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
            .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().setEndpoint("http://0.0.0.0:4317").build()).build())
            .setResource(resource)
            .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
            .addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder().build()).build())
            .setResource(resource)
            .build();

        OpenTelemetryFacade.openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setMeterProvider(sdkMeterProvider)
            .setLoggerProvider(sdkLoggerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal();
    }


    private static void obtainCounter(){
        // Gets or creates a named meter instance
        Meter meter = openTelemetry.meterBuilder("nameAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                .setInstrumentationVersion("1.0.0")
                .build();

        // Build counter e.g. LongCounter
        OpenTelemetryFacade.counter = meter
            .counterBuilder("meternameBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
            .setDescription("meterdescriptionBBBBBBBBBBBBBBBBBBB")
            .setUnit("1")
            .build();

        // It is recommended that the API user keep a reference to Attributes they will record against
        Attributes attributes = Attributes.of(AttributeKey.stringKey("Key"), "SomeWork");

        // Record data
        OpenTelemetryFacade.counter.add(123, attributes);

    }

    public static void setupOpentelemetry() {
        obtainOpentelemetry();
        obtainCounter();
    }





    public static void updateMetrics(){
        inner_counter++;
        System.out.println("      Adding to the counter value: " + Integer.toString(inner_counter));
        OpenTelemetryFacade.counter.add(inner_counter);
    } 
}
