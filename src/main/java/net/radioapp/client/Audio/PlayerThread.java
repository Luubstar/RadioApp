package net.radioapp.client.Audio;

import net.radioapp.client.Audio.ClientPlayer;

public class PlayerThread extends  Thread {

    private ClientPlayer player;
    private boolean canRun = true;
    private boolean running = true;
    private byte[] data = null;
    public  PlayerThread(ClientPlayer p){player = p;}

    @Override
    public void run() {
        int bufferSize = player.line.getBufferSize();
        while(running) {
            synchronized (this){
            while (data == null){
                try {wait();} catch (InterruptedException e) { throw new RuntimeException(e);}}
            if(!running){break;}}

            canRun = true;
            int totalBytesRead = 0;
            int bytesReaded;
            boolean calledForMore = false;

            while (totalBytesRead < data.length && canRun) {
                int read = Math.min(bufferSize, data.length - totalBytesRead);
                if (read % 4 != 0) {read -= read % 4;}

                bytesReaded = player.line.write(data, totalBytesRead, read);
                totalBytesRead += bytesReaded;

                if (totalBytesRead >= data.length * 0.25 && !calledForMore) {
                    calledForMore = true;
                    player.pedirMas();
                }

                if (totalBytesRead >= data.length || read == 0) { break;}
            }
            if (!calledForMore) {player.pedirMas();}

            data = null;
            player.isPlaying = false;
            canRun = false;
        }
    }
    public synchronized void execute(byte[] d){data = d; notify();}
    public boolean isStoped(){return !canRun;}
    public synchronized  void stopPlayer(){canRun = false;}
}
