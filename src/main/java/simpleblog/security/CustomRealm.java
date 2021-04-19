package simpleblog.security;



import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import simpleblog.entities.Role;
import simpleblog.entities.User;
import simpleblog.services.UserService;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = principalCollection.getPrimaryPrincipal().toString();
        User user = userService.findUserByName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role :user.getRoles() ){
            simpleAuthorizationInfo.addRole(role.getName());
        }

        return simpleAuthorizationInfo;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        if (authenticationToken.getPrincipal().toString() == null) {
            return null;
        }
        String username = authenticationToken.getPrincipal().toString();
        User user = userService.findUserByName(username);
        if (user == null) {
            return null;
        }
        else{
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),  ByteSource.Util.bytes(user.getPasswordSalt().toString()),getName());
            return simpleAuthenticationInfo;
            
        }

    }


}