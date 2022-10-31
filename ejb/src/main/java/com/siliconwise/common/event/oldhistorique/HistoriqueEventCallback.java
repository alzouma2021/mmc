package com.siliconwise.common.event.oldhistorique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;

/** call back de l'evenement pour l'historisation des actions sur les entités 
 * Class must be implemented by any event initiator
 * @author sysadmin
 *
 */
public abstract class HistoriqueEventCallback implements Serializable {

	private static final long serialVersionUID = 735482874434082202L;

	/** Call back method if evebnt has been sucessfully processed
	 * @param isSucess true if event have been sucessfully processed
	 * @param msgList list of returned messages
	 */
	
	public abstract void onSucess(List<NonLocalizedStatusMessage> msgList) ;
	//TODO mettre un logger pour afficher le nombre de tentative àpres lequel il y'a eu succés d'historisation
	
	/** Call back method if evebnt has been unsucessfull processed
	 * @param isSucess true if event have been sucessfully processed
	 * @param msgList list of returned messages
	 */
	public abstract void onFailure(
			HistoriserEventPayload<IEntityStringkey> payload, 
			List<NonLocalizedStatusMessage> msgList) ;
	
	//TODO A implementer
			// si le nombre maxi de tentative n'et pas encore atteint encore 
			// atteint:
			//		1. on patiente xx temps (c'est dans le fichie de confirg de l'app
			//		2. incrementer le nombre d'essai infructueux
			//		3. on fait une nouvelle tentative d'historisation 
			//				en levant un evenement. 
			//				Le nimbre de tentative est dan sle payload
			

}
