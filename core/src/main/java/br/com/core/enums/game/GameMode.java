package br.com.core.enums.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum GameMode {
    NODEBUFF("nodebuff", "NoDebuff","rO0ABXcEAAAAJHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QADURJQU1PTkRfU1dPUkRzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAh0AAltZXRhLXR5cGV0AAhlbmNoYW50c3VxAH4ABgAAAAN0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUNzcQB+AAN1cQB+AAYAAAADdAAKREFNQUdFX0FMTHQAC0ZJUkVfQVNQRUNUdAAKRFVSQUJJTElUWXVxAH4ABgAAAANzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAA3NxAH4AHAAAAAJxAH4AHnNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJdAAGYW1vdW50dXEAfgAGAAAAA3EAfgAMdAALRU5ERVJfUEVBUkxzcQB+ABwAAAAQc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAl0AAZkYW1hZ2V1cQB+AAYAAAADcQB+AAx0AAZQT1RJT05zcgAPamF2YS5sYW5nLlNob3J0aE03EzRg2lICAAFTAAV2YWx1ZXhxAH4AHSBDc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtICJzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACN1cQB+AAYAAAADcQB+AAx0AA1HT0xERU5fQ0FSUk9Uc3EAfgAcAAAAQHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC0gInNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC0gInNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC1AJXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACp1cQB+AAYAAAADcQB+AAxxAH4ALHNxAH4ALUAlc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AKnVxAH4ABgAAAANxAH4ADHEAfgAsc3EAfgAtQCVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAqdXEAfgAGAAAAA3EAfgAMcQB+ACxzcQB+AC0gIg=="),
    SKYWARS("skywars", "SkyWars","rO0ABXcEAAAAJHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QADURJQU1PTkRfU1dPUkRzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAh0AAltZXRhLXR5cGV0AAhlbmNoYW50c3VxAH4ABgAAAAN0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUNzcgA3Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVCaU1hcCRTZXJpYWxpemVkRm9ybQAAAAAAAAAAAgAAeHEAfgADdXEAfgAGAAAAAXQACkRBTUFHRV9BTEx1cQB+AAYAAAABc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAFzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXQABmFtb3VudHVxAH4ABgAAAANxAH4ADHQAC0VOREVSX1BFQVJMc3EAfgAbAAAABXNxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEAfgAMdAALRklTSElOR19ST0RzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXQABmRhbWFnZXVxAH4ABgAAAANxAH4ADHQABlBPVElPTnNyAA9qYXZhLmxhbmcuU2hvcnRoTTcTNGDaUgIAAVMABXZhbHVleHEAfgAcIEJzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAtcQB+ACF1cQB+AAYAAAAEcQB+AAx0AARXT09Ec3EAfgAwAAJzcQB+ABsAAABAc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ALXEAfgAhdXEAfgAGAAAABHEAfgAMdAAMTU9OU1RFUl9FR0dTc3EAfgAwAAFxAH4AOHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACF1cQB+AAYAAAADcQB+AAx0AAxHT0xERU5fQVBQTEVxAH4AJHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACF1cQB+AAYAAAADcQB+AAx0AAlTTk9XX0JBTExzcQB+ABsAAAAQc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AIXVxAH4ABgAAAANxAH4ADHQAC0NPT0tFRF9CRUVGcQB+ADhwcHBwcHBwcHBwcHBwcHBwcHBzcQB+AABzcQB+AAN1cQB+AAYAAAACcQB+AAhxAH4ACXVxAH4ABgAAAAJxAH4ADHQADFdBVEVSX0JVQ0tFVHNxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEAfgAMdAALTEFWQV9CVUNLRVRwcHBwcHBw");
    private String name;
    private String displayName;
    private String defaultInventoryEncoded;

    GameMode(String name, String displayName, String defaultInventoryEncoded) {
        this.name = name;
        this.displayName = displayName;
        this.defaultInventoryEncoded = defaultInventoryEncoded;
    }

    private static final GameMode[] values;

    static {
        values = values();
    }

    public static GameMode getByName(String name) {
        return Arrays.stream(values).filter(gamemode -> gamemode.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}