package com.asset.fc.common.parser;

import com.asset.fc.common.model.Job;
import java.io.File;

public interface Parse {

    /**
     *
     * @param file
     * @return
     * @throws java.lang.Exception
     */
    public Job Prase(File file) throws Exception;

    public Job Prase(String body, String ext) throws Exception;

};
