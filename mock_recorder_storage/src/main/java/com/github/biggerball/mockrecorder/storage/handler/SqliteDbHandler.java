package com.github.biggerball.mockrecorder.storage.handler;

import com.github.biggerball.mockrecorder.storage.entity.ReadDbRecord;
import com.github.biggerball.mockrecorder.storage.entity.ReadDbReq;
import com.github.biggerball.mockrecorder.storage.entity.ResultVO;
import com.github.biggerball.mockrecorder.storage.mapper.ParamRecordMapper;
import com.github.biggerball.mockrecorder.storage.entity.InvokeRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/db")
@Slf4j
public class SqliteDbHandler extends DbHandler implements InitializingBean {

    @Resource
    ParamRecordMapper paramRecordMapper;

    @Value("${spring.datasource.url}")
    String url;

    @PostMapping("/write")
    @ResponseBody
    public ResultVO write(@RequestBody InvokeRecord invokeRecord) {
        try {
            return asyncWriteDb(() -> {
                try {
                    paramRecordMapper.insert(invokeRecord);
                } catch (Exception e) {
                    log.error("SqliteDbHandler asyncWriteDb err", e);
                }
            });
        } catch (Exception e) {
            log.error("SqliteDbHandler write err", e);
            return ResultVO.error(e.getMessage());
        }
    }

    @PostMapping("/read")
    @ResponseBody
    public ResultVO read(@RequestBody ReadDbReq req) {
        try {
            List<InvokeRecord> data = paramRecordMapper.read(req, parseDate(req.getFromTime()), parseDate(req.getToTime()));
            List<ReadDbRecord> results = new ArrayList<>(data.size());
            for (InvokeRecord record : data) {
                results.add(new ReadDbRecord(record));
            }
            return ResultVO.success(results);
        } catch (Exception e) {
            log.error("SqliteDbHandler read err", e);
            return ResultVO.error(e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Matcher m = Pattern.compile("jdbc:sqlite:(.*)").matcher(url);
        if (m.find()) {
            File dbFile = new File(m.group(1));
            // 如果父路径不存在，则先创建父路径
            if(!dbFile.getParentFile().exists()){
                boolean create = dbFile.getParentFile().mkdirs();
                if (!create) {
                    throw new RuntimeException(String.format("sqlite dir %s create fail", dbFile.getAbsolutePath()));
                }
            }
            crateConfigFile(dbFile.getParentFile().getPath());
        } else {
            throw new RuntimeException("sqlite url format error, should be like 'jdbc:sqlite:/data/db/test.db'");
        }

        paramRecordMapper.createTable();
    }

    private Long parseDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        try {
            return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss").getTime();
        } catch (Exception e) {
            return null;
        }
    }
}
