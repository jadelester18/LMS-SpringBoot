package com.major.revalida.appuser.admin.crud.room;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
	
	@Autowired
    private RoomRepository roomRepository;
	
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String generateRandomString(int length) {
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
        int randomIndex = RANDOM.nextInt(ALPHANUMERIC_CHARS.length());
        sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
      }
      return sb.toString();
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public Room createRoom(Room room) {
    	room.setRoomCode(generateRandomString(5));
        return roomRepository.save(room);
    }

    public Room updateRoom(Long roomId, Room room) {
        room.setRoomId(roomId);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
    
}
