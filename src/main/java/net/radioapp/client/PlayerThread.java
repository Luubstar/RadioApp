package net.radioapp.client;

public class PlayerThread extends  Thread {

    ClientPlayer player;
    boolean canRun = true;
    public  PlayerThread(ClientPlayer p){player = p;}

    @Override
    public void run() {
        int bufferSize = player.line.getBufferSize();
        int totalBytesRead = 0;
        int bytesReaded;
        boolean calledForMore = false;

        player.lockLectura.lock();

        try {
            byte[] buffer = player.data.toByteArray();
            player.data.reset();

            while (totalBytesRead < buffer.length && canRun) {
                int read =  Math.min(bufferSize, buffer.length - totalBytesRead);
                if(read % 4 != 0){read -= read%4; System.out.println("read extraño");}
                bytesReaded = player.line.write(buffer, totalBytesRead, read);
                totalBytesRead += bytesReaded;

                if (totalBytesRead >= buffer.length || read == 0) {
                    break;
                } else if (totalBytesRead >= buffer.length * 0.5 && !calledForMore) {
                    calledForMore = true;
                    player.pedirMas();
                } else if (totalBytesRead == 0) {
                    System.exit(-1);
                }
            }
            player.pedirMas();
        }
        finally {player.lockLectura.unlock();}

        player.isPlaying = false;
        player.line.drain();
    }


    public synchronized  void stopPlayer(){canRun = false;}
}
