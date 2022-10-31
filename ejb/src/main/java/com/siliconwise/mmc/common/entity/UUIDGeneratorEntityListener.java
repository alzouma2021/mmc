package com.siliconwise.mmc.common.entity;

import java.util.UUID;

import javax.persistence.PrePersist;

import com.siliconwise.common.entity.IEntityStringkey;


/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public class UUIDGeneratorEntityListener {

	@PrePersist
	public static void generateUUIDKey(Object object)
	{
		if (!IEntityStringkey.class.isInstance(object))
		{
			throw new IllegalArgumentException("Not an IEntityStringKey instance.");
		}
		
		IEntityStringkey entity = (IEntityStringkey) object ;
		
		// only update if it is a really new entity and no a detached one being reattached
		if (entity.getId() == null) entity.setId(UUID.randomUUID().toString());
	}
	
}
