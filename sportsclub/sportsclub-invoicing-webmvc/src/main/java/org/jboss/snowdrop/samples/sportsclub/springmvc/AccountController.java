package org.jboss.snowdrop.samples.sportsclub.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import org.jboss.snowdrop.samples.sportsclub.ejb.SubscriptionService;
import org.jboss.snowdrop.samples.sportsclub.domain.entity.Account;

import javax.ejb.EJB;
import java.util.List;

/**
 * @author <a href="mailto:lvlcek@redhat.com">Lukas Vlcek</a>
 */
@Controller
public class AccountController
{

   private static final String[] invoiceStatus;

   static {
      invoiceStatus = new String[]{UserInput.INVOICE_WITHOUT, UserInput.INVOICE_WITH};
   }

   @EJB(mappedName = "sportsclub/SubscriptionService")
   SubscriptionService subscriptionService;


   /**
    * Just forwarding to the view with fresh-empty model.
    *
    * @param userInput
    * @return
    */
   @RequestMapping(value = "/searchAccount.do", method = RequestMethod.GET)
   ModelMap enterPage(UserInput userInput)
   {
      ModelMap model = new ModelMap();
      model.addAttribute(userInput)
           .addAttribute(invoiceStatus);
      return model;
   }

   @RequestMapping(value = "/searchAccount.do", method = RequestMethod.POST)
   ModelMap updateAccount(UserInput userInput)
   {
      String nameFragment = userInput.getNameFragment();
      Integer maxAccountNum = userInput.getMaxAccountNum();
      boolean currentInvoice = (UserInput.INVOICE_WITH.equals(userInput.getInvoiceStatus()) ? true : false);

      System.out.println("****** currentInvoice = " + currentInvoice);

      List<Account> accountList = subscriptionService.findAccountsBySubscriberName(nameFragment, 0, maxAccountNum, currentInvoice);

      ModelMap model = new ModelMap();
      model.addAttribute(userInput)
           .addAttribute(accountList)
           .addAttribute(invoiceStatus);
      return model;
   }

   @RequestMapping(value = "/accountDetail.do", method = RequestMethod.GET)
   ModelMap getAccountDetail(@RequestParam("id") String id)
   {
      Account account = subscriptionService.findAccountById(Long.parseLong(id));

      ModelMap model = new ModelMap();
      model.addAttribute(account);
      return model;
   }

   @RequestMapping(value = "/generateInvoice.do", method = RequestMethod.POST)
   ModelMap generateInvoice(@RequestParam("id") String id)
   {
      ModelMap model = new ModelMap();
      model.addAttribute("id",id);
      return model;
   }

}
