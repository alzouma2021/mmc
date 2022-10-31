package com.siliconwise.mmc.message;

import java.time.LocalDateTime;

public interface INonLocalizedStatusMessage {

	public abstract StatusMessageType getStatus();

	public abstract void setStatus(StatusMessageType status);

	public abstract String getMessage();

	public abstract void setMessage(String message) ;

	public abstract LocalDateTime getDateHeure() ;

	public abstract void setDateHeure(LocalDateTime dateHeure) ;

}