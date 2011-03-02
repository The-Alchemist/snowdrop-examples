package org.jboss.snowdrop.samples.sportsclub.reservations.webflow.beans;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.jboss.snowdrop.samples.sportsclub.service.EquipmentService;
import org.jboss.snowdrop.samples.sportsclub.domain.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;

/**
 * @author <a href="mailto:lvlcek@redhat.com">Lukas Vlcek</a>
 */
public class EquipmentFilter extends AbstractExtendedDataModelHelper implements Serializable
{
   @Autowired
   private transient EquipmentService equipmentService;

   private Date availableFrom;

   private Date availableTo;

   private Equipment selectedEquipment;

   private Map<Long, Equipment> equipmentMap = new HashMap<Long, Equipment>();

   public EquipmentFilter()
   {
      super();
   }

   public Map<Long, ? extends Object> getDomainObjectMap()
   {
      return equipmentMap;
   }

   public Long getCurrentRowCount()
   {
      if (availableFrom == null && availableTo == null)
      {
         return equipmentService.countAllEquipments();
      }
      else
      {
         return equipmentService.countUnreservedEquipmentsForRange(availableFrom, availableTo, null);
      }
   }

   public void walk(FacesContext facesContext, DataVisitor dataVisitor, Range range, Object argument) throws IOException
   {
      int firstResult = ((SequenceRange) range).getFirstRow();
      int maxResults = ((SequenceRange) range).getRows();
      List<Equipment> equipments = null;
      if (availableFrom == null && availableTo == null)
      {
         equipments = (List<Equipment>) equipmentService.getAllEquipments(firstResult, maxResults);
      }
      else
      {
         equipments = equipmentService.getUnreservedEquipments(availableFrom, availableTo, firstResult, maxResults, null);
      }
      for (Equipment e : equipments)
      {
         Long id = e.getId();
         equipmentMap.put(id, e);
         dataVisitor.process(facesContext, id, argument);
      }
   }

   public Equipment getSelectedEquipment()
   {
      return selectedEquipment;
   }

   public void setSelectedEquipment(Equipment selectedEquipment)
   {
      this.selectedEquipment = selectedEquipment;
   }

   public Date getAvailableFrom()
   {
      return availableFrom;
   }

   public Date getAvailableTo()
   {
      return availableTo;
   }

   public void setAvailableFrom(Date availableFrom)
   {
      this.availableFrom = availableFrom;
   }

   public void setAvailableTo(Date availableTo)
   {
      this.availableTo = availableTo;
   }

   public EquipmentService getEquipmentService()
   {
      return equipmentService;
   }

   public void setEquipmentService(EquipmentService equipmentService)
   {
      this.equipmentService = equipmentService;
   }

   public void reset()
   {

   }
}