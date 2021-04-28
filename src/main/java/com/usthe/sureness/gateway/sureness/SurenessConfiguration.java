package com.usthe.sureness.gateway.sureness;


import com.usthe.sureness.gateway.sureness.support.BasicSubjectReactiveCreator;
import com.usthe.sureness.gateway.sureness.support.JwtSubjectReactiveCreator;
import com.usthe.sureness.gateway.sureness.support.NoneSubjectReactiveCreator;
import com.usthe.sureness.matcher.DefaultPathRoleMatcher;
import com.usthe.sureness.matcher.PathTreeProvider;
import com.usthe.sureness.mgt.SurenessSecurityManager;
import com.usthe.sureness.processor.DefaultProcessorManager;
import com.usthe.sureness.processor.Processor;
import com.usthe.sureness.processor.support.JwtProcessor;
import com.usthe.sureness.processor.support.NoneProcessor;
import com.usthe.sureness.processor.support.PasswordProcessor;
import com.usthe.sureness.provider.SurenessAccountProvider;
import com.usthe.sureness.provider.ducument.DocumentAccountProvider;
import com.usthe.sureness.provider.ducument.DocumentPathTreeProvider;
import com.usthe.sureness.subject.SubjectCreate;
import com.usthe.sureness.subject.SubjectFactory;
import com.usthe.sureness.subject.SurenessSubjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * sureness config
 * @author tomsun28
 * @date 23:38 2019-09-29
 */
@Configuration
public class SurenessConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SurenessConfiguration.class);

    public SurenessConfiguration() {
        init();
    }

    private void init() {

        // process init
        // account provider
        SurenessAccountProvider accountProvider = new DocumentAccountProvider();
        List<Processor> processorList = new LinkedList<>();
        NoneProcessor noneProcessor = new NoneProcessor();
        processorList.add(noneProcessor);
        JwtProcessor jwtProcessor = new JwtProcessor();
        processorList.add(jwtProcessor);
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        passwordProcessor.setAccountProvider(accountProvider);
        processorList.add(passwordProcessor);
        DefaultProcessorManager processorManager = new DefaultProcessorManager(processorList);
        if (logger.isDebugEnabled()) {
            logger.debug("DefaultProcessorManager init");
        }

        // pathRoleMatcher init
        // pathTree resource provider
        PathTreeProvider pathTreeProvider = new DocumentPathTreeProvider();
        DefaultPathRoleMatcher pathRoleMatcher = new DefaultPathRoleMatcher();
        pathRoleMatcher.setPathTreeProvider(pathTreeProvider);
        pathRoleMatcher.buildTree();
        if (logger.isDebugEnabled()) {
            logger.debug("DefaultPathRoleMatcher init");
        }

        // SubjectFactory init
        SubjectFactory subjectFactory = new SurenessSubjectFactory();
        List<SubjectCreate> subjectCreates = Arrays.asList(
                new NoneSubjectReactiveCreator(),
                new BasicSubjectReactiveCreator(),
                new JwtSubjectReactiveCreator());
        subjectFactory.registerSubjectCreator(subjectCreates);
        if (logger.isDebugEnabled()) {
            logger.debug("SurenessSubjectFactory init");
        }

        // surenessSecurityManager init
        SurenessSecurityManager securityManager = SurenessSecurityManager.getInstance();
        securityManager.setPathRoleMatcher(pathRoleMatcher);
        securityManager.setSubjectFactory(subjectFactory);
        securityManager.setProcessorManager(processorManager);
        if (logger.isDebugEnabled()) {
            logger.debug("SurenessSecurityManager init");
        }
    }


}
