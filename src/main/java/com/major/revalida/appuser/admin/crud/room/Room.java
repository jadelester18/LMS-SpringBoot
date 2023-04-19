package com.major.revalida.appuser.admin.crud.room;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long roomId;
	private String roomCode;
    private String roomName;
    
}
