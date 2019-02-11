package com.odenktools.productservice.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<PageImpl> {

	@Override
	public void serialize(PageImpl value, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("number", value.getNumber());
		jsonGenerator.writeNumberField("numberOfElements", value.getNumberOfElements());
		jsonGenerator.writeNumberField("totalElements", value.getTotalElements());
		jsonGenerator.writeNumberField("totalPages", value.getTotalPages());
		jsonGenerator.writeNumberField("size", value.getSize());
		jsonGenerator.writeFieldName("content");
		serializers.defaultSerializeValue(value.getContent(), jsonGenerator);
		jsonGenerator.writeEndObject();
	}

}