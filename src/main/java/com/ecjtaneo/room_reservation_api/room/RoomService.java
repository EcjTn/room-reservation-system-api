package com.ecjtaneo.room_reservation_api.room;

import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceConflictException;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceNotFoundException;
import com.ecjtaneo.room_reservation_api.room.dto.RoomCreationDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicPartialResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomUpdateDto;
import com.ecjtaneo.room_reservation_api.room.model.Room;
import com.ecjtaneo.room_reservation_api.room.mapper.RoomMapper;
import com.ecjtaneo.room_reservation_api.room.model.RoomStatus;
import com.ecjtaneo.room_reservation_api.room.model.RoomType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public MessageResponseDto createNewRoom(RoomCreationDto roomCreationDto) {
        if(roomRepository.existsByRoomNumber(roomCreationDto.roomNumber())) throw new ResourceConflictException("Room already exists");
        Room room = roomMapper.toEntity(roomCreationDto);
        roomRepository.save(room);

        return new MessageResponseDto("Room successfully created.");
    }

    @Cacheable("rooms")
    public List<RoomPublicPartialResponseDto> getRooms() {
        return roomMapper.toPartialDtoList(roomRepository.findTop10ByOrderByIdDesc());
    }

    @Cacheable(value = "rooms", key = "#lastSeenId")
    public List<RoomPublicPartialResponseDto> getRoomsBefore(Long lastSeenId) {
        return roomMapper.toPartialDtoList(roomRepository.findTop10ByIdLessThanOrderByIdDesc(lastSeenId));
    }

    public List<RoomPublicResponseDto> getRoomsByFilters(RoomStatus status, RoomType type) {
        return roomMapper.toDtoList(roomRepository.findTop10ByStatusAndTypeOrderByIdDesc(status, type));
    }

    public List<RoomPublicResponseDto> getRoomsByFiltersBefore(RoomStatus status, RoomType type, Long lastSeenId) {
        return roomMapper.toDtoList(roomRepository.findTop10ByStatusAndTypeAndIdLessThanOrderByIdDesc(status, type, lastSeenId));
    }

    @Cacheable(value = "room", key = "#roomNumber")
    public RoomPublicResponseDto findRoomByRoomNumber(String roomNumber) {
        return roomMapper.toDtoSingle(roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found.")));
    }

    @Transactional
    public MessageResponseDto deleteByRoomNumber(String roomNumber) {
        roomRepository.deleteByRoomNumber(roomNumber);
        return new MessageResponseDto("Room successfully deleted.");
    }

    //only used by admins, manual update when actual rooms need changes.
    @Transactional
    @CacheEvict(value = "room", key = "#roomNumber")
    public MessageResponseDto updateRoom(String roomNumber, RoomUpdateDto roomUpdateDto){
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setStatus(roomUpdateDto.status());
        room.setType(roomUpdateDto.type());
        room.setPricePerNight(roomUpdateDto.price_per_night());

        return new MessageResponseDto("Room successfully updated.");
    }

    public Room findAvailableRoom(String roomNumber) {
        return roomRepository.findByRoomNumberAndStatus(roomNumber, RoomStatus.AVAILABLE)
                .orElseThrow(() -> new ResourceNotFoundException("No available rooms found."));
    }

    @Transactional
    @CacheEvict(value = "room", key = "#roomNumber")
    public int setRoomBooked(String roomNumber) {
        return roomRepository.updateStatusByRoomNumber(roomNumber, RoomStatus.BOOKED);
    }

    @Transactional
    @CacheEvict(value = "room", key = "#roomNumber")
    public int setRoomOccupied(String roomNumber) {
        return roomRepository.updateStatusByRoomNumber(roomNumber, RoomStatus.OCCUPIED);
    }

    @Transactional
    @CacheEvict(value = "room", key = "#roomNumber")
    public int setRoomAvailable(String roomNumber) {
        return roomRepository.updateStatusByRoomNumber(roomNumber, RoomStatus.AVAILABLE);
    }

    public long getAvailableRoomsCount() {
        return roomRepository.countByStatus(RoomStatus.AVAILABLE);
    }

    public long getRoomsCount() {
        return roomRepository.count();
    }
}
