package cs455.scaling.client;

import cs455.scaling.helpers.Constants;
import cs455.scaling.helpers.Hasher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

/**
 * This class enables each Client to send randomly generated payloads of 8KB each at regular intervals to the lone Server
 */
public class SendMessageAsync implements Runnable {
    private Client client;
    private SocketChannel channel;
    private Random rd;
    private int sleepTime;

    SendMessageAsync(Client client, int messagingRate, SocketChannel channel) {
        this.client = client;
        this.channel = channel;
        this.rd = new Random();
        this.sleepTime = Constants.PER_SECOND / messagingRate; //Calculate how much time the client needs to wait before sending the next message.
    }


    @Override
    public void run() {
        while (true) {
            byte[] randomBytes = new byte[Constants.BYTES_PER_MESSAGE];
            rd.nextBytes(randomBytes); //Generate random payload

            String hashOfPayload = Hasher.SHA1FromBytes(randomBytes); //Calculate SHA-1 hash of random payload
            hashOfPayload = String.format("%40s", hashOfPayload).replace(" ", "-"); //Pad hash to make it length 40. Essential for synchronizing sizes of messages
            client.updateHashes(hashOfPayload); //Invoke update hashes method in Client

            ByteBuffer byteBuffer = ByteBuffer.wrap(randomBytes); //Wrap random payload into a byte buffer

            while (byteBuffer.hasRemaining()) {
                try {
                    channel.write(byteBuffer); //Write byte buffer to the socket channel, sending it to the server
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            client.incrementSentCount();

            try {
                Thread.sleep(sleepTime); //Wait for sleepTime(in millis) before sending next message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
