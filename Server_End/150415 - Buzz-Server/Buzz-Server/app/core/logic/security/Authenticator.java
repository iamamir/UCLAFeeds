
package core.logic.security;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import models.dto.Request;
import util.SHA256Generator;
import constant.Constants;


public class Authenticator
{

    public static String generateHash(Map<String, String> requestMap)
    {
        if (requestMap != null)
        {
            List<String> keys = new ArrayList<String>(requestMap.keySet());
            Collections.sort(keys);
            StringBuilder values = new StringBuilder();
            for (String key : keys)
            {
                if (!key.equals(Request.Params.HASH.getValue()))
                {
                    values.append(requestMap.get(key));
                }
            }
            values.append(Constants.salt);
            String strBeforeHash = values.toString();
            try
            {
                String hash = SHA256Generator.SHA256(strBeforeHash);
                return hash;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }
}
