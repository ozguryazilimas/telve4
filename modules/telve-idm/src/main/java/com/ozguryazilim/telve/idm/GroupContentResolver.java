/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.contact.AbstractContactSource;
import com.ozguryazilim.telve.contact.Contact;
import com.ozguryazilim.telve.contact.ContactSource;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.group.GroupRepository;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Notification için grup üyelerini Contact'a çevirir.
 *
 * @author Hakan Uygun
 */
@ContactSource(name = "group")
public class GroupContentResolver extends AbstractContactSource {

    private static final String ID = "id";

    @Inject
    private UserGroupRepository userGroupRepository;

    @Inject
    private GroupRepository groupRepository;

    @Override
    public void resolve(Map<String, String> params, List<Contact> result) {

        String groupCode = params.get(ID);
        
        //Parametre ID groupCode içerecek
        if (!Strings.isNullOrEmpty(groupCode)) {
            //Önce bu koda sahip grupları bulalım
            List<Group> groups = groupRepository.findByCode(groupCode);
            groups.stream().map((grp) -> userGroupRepository.findByGroup(grp)).forEachOrdered((ugs) -> {
                //Şimdi de bu gruplara dahil kullanıcıları
                ugs.forEach((ug) -> {
                    resolveUser(ug.getUser(), result);
                });
            });
        }
    }

    protected void resolveUser(User user, List<Contact> result) {

        if (user != null) {
            Contact c = new Contact();
            c.setSource(getClass().getSimpleName());
            c.setType("User");
            c.setId(user.getLoginName());
            c.setFirstname(user.getFirstName());
            c.setLastname(user.getLastName());
            c.setEmail(user.getEmail());
            c.setMobile(user.getMobile());
            result.add(c);
        }

    }

}
