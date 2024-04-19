package net.radioapp.web.Network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class UDPDataArray {
    byte[] data;
    int editingPosition = 0;
    public static final int CHUNKSIZE = 2048;
    public static final int METADATASIZE = 5;
    public UDPDataArray(int size){
        data = new byte[size];
    }
    public UDPDataArray(){data = new byte[CHUNKSIZE-METADATASIZE];}
    public UDPDataArray(byte[] data){
        this.data = data;
    }

    public void addData(byte v){addData(new byte[]{v});}

    public void addData(byte[] values){
        for (byte value : values) {
            data[editingPosition] = value;
            editingPosition++;
        }
        if (editingPosition > CHUNKSIZE){
            throw new RuntimeException("El tamaño de un paquete ha superado el esperado. Revisa el tamaño de los metadatos");
        }
    }

    public void addData(byte[] values, int start, int end){
        for (int i = start; i <= end; i++) {
            data[editingPosition] = values[i];
            editingPosition++;
        }
        if (editingPosition > CHUNKSIZE){
            throw new RuntimeException("El tamaño de un paquete ha superado el esperado. Revisa el tamaño de los metadatos");
        }
    }
    public byte[] getData(int start, int end){
        if(end < start){throw  new RuntimeException("Error al leer datos de un paquete: Fin antes que inicio");}
        byte[] res = new byte[end-start + 1];
        for(int i = 0; i < res.length ; i++) {res[i] = data[i + start]; }
        return res;
    }

    public byte[] getContent(){return getData(UDPDataArray.METADATASIZE, UDPDataArray.CHUNKSIZE-1);}
    public PackageTypes getType(){return PackageTypes.obtenerTipoPorCodigo(data[0]);}
    public byte[] getData(){return data;}

    public static int byteToInt(byte[] c){
        if (c.length % 4 != 0){
            byte[] temp = new byte[c.length + c.length%4];
            System.arraycopy(c,0,temp,c.length%4, c.length);
            c = temp;
        }
        return ByteBuffer.wrap(c).getInt();
    }
    public static int byteToIntLittleEndian(byte[] c){
        if (c.length % 4 != 0){
            byte[] temp = new byte[c.length + c.length%4];
            System.arraycopy(c,0,temp,0, c.length);
            c = temp;
        }
        return ByteBuffer.wrap(c).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static byte[] intToByte(int c){
        return ByteBuffer.allocate(4).putInt(c).array();
    }

    public static String byteToString(byte[] b){return  new String(b, StandardCharsets.UTF_8);}
    public static byte[] changeToBigEndian(byte[] b){
        return intToByte(byteToIntLittleEndian(b));
    }

}
