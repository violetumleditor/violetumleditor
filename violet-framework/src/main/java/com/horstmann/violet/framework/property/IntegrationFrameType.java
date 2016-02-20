package com.horstmann.violet.framework.property;


public class IntegrationFrameType {

    private final String name;

    private IntegrationFrameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static final IntegrationFrameType ALT = new IntegrationFrameType("alt");
    public static final IntegrationFrameType ELSE = new IntegrationFrameType("else");
    public static final IntegrationFrameType LOOP = new IntegrationFrameType("loop");
    public static final IntegrationFrameType BREAK = new IntegrationFrameType("break");
    public static final IntegrationFrameType OPT = new IntegrationFrameType("opt");
    public static final IntegrationFrameType PAR = new IntegrationFrameType("par");
    public static final IntegrationFrameType NEG = new IntegrationFrameType("neg");
    public static final IntegrationFrameType STRICT = new IntegrationFrameType("strict");
    public static final IntegrationFrameType CRITICAL = new IntegrationFrameType("critical");
}