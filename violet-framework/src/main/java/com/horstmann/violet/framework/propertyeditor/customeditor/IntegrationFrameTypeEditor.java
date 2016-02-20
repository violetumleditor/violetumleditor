package com.horstmann.violet.framework.propertyeditor.customeditor;

import com.horstmann.violet.framework.propertyeditor.CustomPropertyEditorSupport;
import com.horstmann.violet.framework.property.IntegrationFrameType;

/**
 * A property editor for the integration frame type.
 */
public class IntegrationFrameTypeEditor extends CustomPropertyEditorSupport {

    /**
     * Default constructor that sets defined names and values.
     */
    public IntegrationFrameTypeEditor() {
        super(NAMES, VALUES);
    }

    /**
     * frame type labels
     */
    public static final String[] NAMES = {
            "alt",
            "else",
            "loop",
            "break",
            "opt",
            "par",
            "neg",
            "strict",
            "critical"
    };

    /**
     * frame type technical values
     */
    public static final Object[] VALUES = {
            IntegrationFrameType.ALT,
            IntegrationFrameType.ELSE,
            IntegrationFrameType.LOOP,
            IntegrationFrameType.BREAK,
            IntegrationFrameType.OPT,
            IntegrationFrameType.PAR,
            IntegrationFrameType.NEG,
            IntegrationFrameType.STRICT,
            IntegrationFrameType.CRITICAL
    };

}