package miniplc0java.navm;

import miniplc0java.analyser.Function;
import org.checkerframework.checker.units.qual.A;

import javax.xml.crypto.dsig.dom.DOMSignContext;
import java.io.*;
import java.util.ArrayList;

public class OZero {
    private int magic = 0x72303b3e;
    private int version = 0x00000001;
    private ArrayList<GlobalDef> globals;
    private ArrayList<FunctionDef> functions;

    private ArrayList<Byte> byteInstructionsList;

    public OZero() {
        this.functions = new ArrayList<>();
        this.globals = new ArrayList<>();
        this.byteInstructionsList = new ArrayList<>();
    }

    public void addFunctionDef(FunctionDef functionDef) {
        this.functions.add(functionDef);
    }

    public void addGlobalDef(GlobalDef globalDef) {
        this.globals.add(globalDef);
    }
    public int getNameOfFunctionDef(FunctionDef functionDef) {
        for (int i = 0; i < functions.size(); i ++) {
            if (functions.get(i) == functionDef) {
                return i;
            }
        }
        return -1;
    }

    public void outputOzero() throws IOException {
        File file = new File("/Users/apple/Documents/MyCompiler/miniplc0-java/o0.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);


        System.out.println();
        System.out.format("%8x\n", magic);
        dataOutputStream.writeByte(magic);
        System.out.format("%08x\n", version);
        dataOutputStream.writeByte(version);
        System.out.println();
        System.out.format("%08x\n", globals.size());
        System.out.println();
        for (GlobalDef globalDef : globals) {
            System.out.format("%02x\n", globalDef.getIs_const());
            System.out.format("%08x\n", globalDef.getValue().size());
            for (Integer integer : globalDef.getValue()) {
                System.out.format("%02x ", integer);
            }
            System.out.println();
            System.out.println();
        }

        for (FunctionDef functionDef : functions) {
            System.out.format("%08x\n", functionDef.getName());
            System.out.format("%08x\n", functionDef.getReturn_slots());
            System.out.format("%08x\n", functionDef.getParam_slots());
            System.out.format("%08x\n", functionDef.getLoc_slots());
            System.out.format("%08x\n", functionDef.getBody().size());

            for (NavmInstruction navmInstruction : functionDef.getBody()) {
                System.out.format("%02x ", navmInstruction.getOpcode());
                if (navmInstruction.isHasParam()) {
                    System.out.format("%08x", navmInstruction.getParam());
                }
                System.out.println();

            }
            System.out.println();
        }

    }

    public void o0OutputToFile () throws IOException {
        File file = new File("/Users/apple/Documents/MyCompiler/miniplc0-java/binary_o0.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        //System.out.format("%8x\n", magic);
        trans32ToByte(magic);

        //System.out.format("%08x\n", version);
        trans32ToByte(version);

        //System.out.format("%08x\n", globals.size());
        trans32ToByte(globals.size());

        //System.out.println();
        for (GlobalDef globalDef : globals) {
            //System.out.format("%02x\n", globalDef.getIs_const());
            trans8ToByte(globalDef.getIs_const());

            //System.out.format("%08x\n", globalDef.getValue().size());
            trans32ToByte(globalDef.getValue().size());

            for (Integer integer : globalDef.getValue()) {
                //System.out.format("%02x ", integer);
                trans8ToByte(integer);
            }
//            System.out.println();
//            System.out.println();
        }

        for (FunctionDef functionDef : functions) {
            //System.out.format("%08x\n", functionDef.getName());
            trans32ToByte(functionDef.getName());

            //System.out.format("%08x\n", functionDef.getReturn_slots());
            trans32ToByte(functionDef.getReturn_slots());

            //System.out.format("%08x\n", functionDef.getParam_slots());
            trans32ToByte(functionDef.getParam_slots());

            //System.out.format("%08x\n", functionDef.getLoc_slots());
            trans32ToByte(functionDef.getLoc_slots());

            //System.out.format("%08x\n", functionDef.getBody().size());
            trans32ToByte(functionDef.getBody().size());

            for (NavmInstruction navmInstruction : functionDef.getBody()) {
                //System.out.format("%02x ", navmInstruction.getOpcode());
                trans8ToByte(navmInstruction.getOpcode());

                if (navmInstruction.isHasParam()) {
                    //System.out.format("%08x", navmInstruction.getParam());
                    trans64ToByte(navmInstruction.getParam());
                }
                //System.out.println();

            }
            //System.out.println();
        }
        byte byteInstructions[] = new byte[this.byteInstructionsList.size()];
        for (int i = 0; i < byteInstructions.length; i ++) {
            byteInstructions[i] = this.byteInstructionsList.get(i);
        }
        dataOutputStream.write(byteInstructions);

    }

    public void trans32ToByte(int fourByteNum) {
        for (int i = 3; i >= 0; i --) {
            this.byteInstructionsList.add(getLowest8Bit(fourByteNum, i));
        }
    }

    public void trans64ToByte(int eightByteNum) {
        for (int i = 7; i >= 0; i --) {
            this.byteInstructionsList.add(getLowest8Bit(eightByteNum, i));
        }
    }

    public void trans8ToByte(int oneByteNum) {
        this.byteInstructionsList.add(getLowest8Bit(oneByteNum, 0));
    }

    public byte getLowest8Bit(int num, int index) {
        return (byte)((num >> (index * 8)) & 0xff);
    }
}
