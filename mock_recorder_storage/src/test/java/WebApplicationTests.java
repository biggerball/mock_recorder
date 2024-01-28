import com.github.biggerball.mockrecorder.storage.entity.ReadDbReq;
import com.github.biggerball.mockrecorder.storage.SpringbootApplication;
import com.github.biggerball.mockrecorder.storage.entity.ResultVO;
import com.github.biggerball.mockrecorder.storage.handler.SqliteDbHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import serializer.Person;
import serializer.Pet;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class WebApplicationTests {

    @Resource
    SqliteDbHandler sqliteDbHandler;

    @Test
    public void contextLoads() {
//        ParamRecord paramRecord = new ParamRecord();
//        paramRecord.setTraceId(UUID.randomUUID().toString());
//        paramRecord.setClassName("WebApplicationTests");
//        paramRecord.setMethodName("contextLoads");
//        paramRecord.setCreateTime(System.currentTimeMillis());
//
//        ParamInfo paramInfo = new ParamInfo();
//        paramInfo.setException(new Param("null", "-"));
//        Param param = new Param("java.lang.String", "123");
//        paramInfo.setRequestParams(Arrays.asList(param, param));
//        paramInfo.setReturnValue(new Param("void", "-"));
//        paramRecord.setParamInfo(paramInfo);
//
//        sqliteDbHandler.write(paramRecord);

    }

    @Test
    public void read() {
        ReadDbReq readDbReq = new ReadDbReq();
        ResultVO read = sqliteDbHandler.read(readDbReq);
    }

    @Test
    public void save() {
        Date birth = new Date();
        Person peter = new Person("Peter", 12);
        peter.setBirth(birth);

        Pet pet = new Pet("Peppa");
        pet.setType("pig");

        peter.setPet(pet);

        Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        String jsonString = gson.toJson(peter);


//        ParamRecord paramRecord = new ParamRecord(UUID.randomUUID().toString(), "WebApplicationTests", "contextLoads", System.currentTimeMillis());
//
//        ParamInfo paramInfo = new ParamInfo();
//        paramInfo.setException(new Param("null", "-"));
//        paramInfo.setRequestParams(Arrays.asList(new Param(peter.getClass().getName(), jsonString), new Param(pet.getClass().getName(), gson.toJson(pet))));
//        paramInfo.setReturnValue(new Param(peter.getClass().getName(), jsonString));
//        paramRecord.setParamInfo(paramInfo);
//
//        sqliteDbHandler.write(paramRecord);
    }

}
