package com.ozguryazilim.telve.contact;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.UserInfo;
import com.ozguryazilim.telve.auth.UserService;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * User bilgisi üzerinden contact resolve eder.
 *
 * @author Hakan Uygun
 */
@ContactSource(name = "user")
public class UserContactSource extends AbstractContactSource {

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String USER_TYPE = "userType";

    @Inject
    private UserService userService;

    /**
     * Kabul edilen parametreler :
     *
     * LoginName
     *
     * @param params
     * @param result
     */
    @Override
    public void resolve(Map<String, String> params, List<Contact> result) {
        
        String p = params.get(ID);
        
        if (Strings.isNullOrEmpty(p)) {
            p = params.get(USERNAME);
        }
        
        if (!Strings.isNullOrEmpty(p)) {
            //Kullanıcı bilgisinden contact oluşturulup result'a ekleniyor.
            Contact c = new Contact();
            //TODO: UserType ( doktor, nurse v.s. ) gelse peşine iyi olur
            UserInfo ui = userService.getUserInfo(p);
            if( ui != null ){
                c.setSource(getClass().getSimpleName());
                c.setType("User");
                c.setId(ui.getLoginName());
                c.setFirstname(ui.getFirstName());
                c.setLastname(ui.getLastName());
                c.setEmail(ui.getEmail());
                c.setMobile(ui.getMobile());
                result.add(c);
            }
        }
    }

}
