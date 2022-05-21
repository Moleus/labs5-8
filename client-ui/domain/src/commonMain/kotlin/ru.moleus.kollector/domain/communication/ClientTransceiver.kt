package ru.moleus.kollector.domain.communication;

import communication.AbstractTransceiver;
import communication.packaging.Message;
import communication.packaging.Response;
import exceptions.ReceivedInvalidObjectException;

import java.nio.channels.SocketChannel;
import java.util.Optional;

class ClientTransceiver(
    socketChannel: SocketChannel
) : AbstractTransceiver(
    socketChannel
) {
    override fun receive(): Optional<Message> {
        return super.readObject().map(this::checkResponseInstance);
    }

    private fun checkResponseInstance(messageObj: Message): Response {
        if (messageObj !is Response) {
            throw ReceivedInvalidObjectException(Response::class.java, messageObj);
        }
        return messageObj;
    }
}
