package net.radioapp.client.Audio;

import net.radioapp.client.Audio.ClientPlayer;

public class PlayerThread extends  Thread {

    private ClientPlayer player;
    private boolean canRun = true;
    private boolean running = true;
    private byte[] data = null;
    private byte[] oldData = new byte[0];
    public  PlayerThread(ClientPlayer p){player = p;}

    @Override
    public void run() {
        int bufferSize = player.line.getBufferSize();
        int totalBytesRead;
        int bytesReaded;
        boolean calledForMore;

        while(running) {
            synchronized (this){
            while (data == null){
                try {wait();} catch (InterruptedException e) { throw new RuntimeException(e);}}
            if(!running){break;}}

            canRun = true;
            totalBytesRead = 0;
            calledForMore = false;

            while (totalBytesRead < data.length && canRun) {
                int read = Math.min(bufferSize, data.length - totalBytesRead);
                if (read % 4 != 0) {read -= read % 4;}

                bytesReaded = player.line.write(data, totalBytesRead, read);
                totalBytesRead += bytesReaded;

                if (totalBytesRead >= data.length * 0.25 && !calledForMore) {
                    calledForMore = true;
                    player.pedirMas();
                }
                if (totalBytesRead >= data.length || read == 0) {
                    oldData = new byte[read];
                    System.arraycopy(data, data.length-read, oldData, 0, read);
                    break;
                }
            }
            if (!calledForMore) {player.pedirMas();}

            data = null;
            canRun = false;
        }
    }
    public synchronized void execute(byte[] d){
        data = new byte[d.length + oldData.length];
        System.arraycopy(oldData, 0, data, 0, oldData.length);
        System.arraycopy(d, 0, data, oldData.length, d.length);
        oldData = new byte[0];
        notify();
    }
    public boolean isStoped(){return !canRun;}
    public synchronized  void stopPlayer(){canRun = false;}
}
