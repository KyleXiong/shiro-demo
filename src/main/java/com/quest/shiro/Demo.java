package com.quest.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        logger.info("Shiro App");
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");

        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("darkhelmet", "ludicrousspeed");
            token.setRememberMe(false);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
            } catch (IncorrectCredentialsException ice) {
            } catch (LockedAccountException lae) {
            } catch (AuthenticationException ae) {
            }

        }

        logger.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        if (currentUser.hasRole("schwartz")) {
            logger.info("May the Schwartz be with you!");
        } else {
            logger.info("Hello, mere mortal.");
        }

        if (currentUser.isPermitted("lightsaber:weild")) {
            logger.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            logger.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            logger.info(
                    "You are permitted to 'drive' the 'winnebago' with license plate (id) 'eagle5'.  "
                            + "Here are the keys - have fun!");
        } else {
            logger.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        currentUser.logout();
        System.exit(0);
    }

}
