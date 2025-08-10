package utils;

import com.example.demo.model.VerifyUserIdentity;
import com.example.demo.service.QuanziService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 认证工具类
 * <p>
 * 用于集中管理用户认证相关的逻辑，如此处改造为核心的OpenID认证状态检查
 */
public class AuthUtil {
    /**
     * 检查用户是否已通过校园认证 (status = 1)
     * 这是新的统一认证检查方法，将取代分散在各个Controller中的重复逻辑。
     *
     * @param openid 用户的微信OpenID
     * @return 如果用户已经通过校园认证，返回 true；否则返回 false。
     */
    public static boolean isUserVerified(String openid, QuanziService quanziService) {
        if (!StringUtils.hasText(openid)) {
            return false;
        }

        List<VerifyUserIdentity> userList = quanziService.getVerifyUserByOpenid(openid);

        if (CollectionUtils.isEmpty(userList)) {
            return false;
        }

        // status = 1 表示已通过校园认证
        VerifyUserIdentity user = userList.get(0);
        return user != null && user.getStatus() == 1;
    }
}