package com.ljz.mapper;

import com.ljz.model.DataRvsdRecord;
import com.ljz.model.DataRvsdRecordTmp;

import java.util.List;

public interface DataRvsdRecordMapper {

    int batchInsert(DataRvsdRecordTmp dataRvsdRecordTmp);

    List<DataRvsdRecord> queryExptSeqNbr(String dataSrcAbbr);

}