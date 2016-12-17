package com.cezarykluczynski.stapi.server.staff.mapper;

import com.cezarykluczynski.stapi.client.v1.soap.StaffRequest;
import com.cezarykluczynski.stapi.model.staff.dto.StaffRequestDTO;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.cezarykluczynski.stapi.server.common.mapper.DateMapper;
import com.cezarykluczynski.stapi.server.common.mapper.EnumMapper;
import com.cezarykluczynski.stapi.server.common.mapper.RequestSortSoapMapper;
import com.cezarykluczynski.stapi.server.configuration.MapstructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapstructConfiguration.class, uses = {DateMapper.class, EnumMapper.class, RequestSortSoapMapper.class})
public interface StaffSoapMapper {

	@Mappings({
			@Mapping(source = "dateOfBirth.from", target = "dateOfBirthFrom"),
			@Mapping(source = "dateOfBirth.to", target = "dateOfBirthTo"),
			@Mapping(source = "dateOfDeath.from", target = "dateOfDeathFrom"),
			@Mapping(source = "dateOfDeath.to", target = "dateOfDeathTo")
	})
	StaffRequestDTO map(StaffRequest staffRequest);

	com.cezarykluczynski.stapi.client.v1.soap.Staff map(Staff series);

	List<com.cezarykluczynski.stapi.client.v1.soap.Staff> map(List<Staff> series);

}
