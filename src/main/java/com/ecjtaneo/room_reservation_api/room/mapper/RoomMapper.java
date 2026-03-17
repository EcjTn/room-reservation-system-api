package com.ecjtaneo.room_reservation_api.room.mapper;

import com.ecjtaneo.room_reservation_api.room.dto.RoomCreationDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicPartialResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.room_reservation_api.room.model.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    public RoomPublicResponseDto toDtoSingle(Room room);
    public List<RoomPublicResponseDto> toDtoList(List<Room> room);
    public List<RoomPublicPartialResponseDto> toPartialDtoList(List<Room> room);
    public Room toEntity(RoomCreationDto roomCreationDto);
}
