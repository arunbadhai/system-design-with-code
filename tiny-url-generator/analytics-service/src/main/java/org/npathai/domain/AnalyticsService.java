package org.npathai.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.DataAccessException;
import org.npathai.model.AnalyticsInfo;
import org.npathai.model.UserInfo;

import java.util.Optional;

public class AnalyticsService {
    private static final Logger LOG = LogManager.getLogger(AnalyticsService.class);
    private final AnalyticsDao dao;

    public AnalyticsService(AnalyticsDao dao) {
        this.dao = dao;
    }

    public void onRedirectionClicked(String id) {
        dao.incrementClick(id);
        LOG.debug("Incremented click count for id:{}", id);
    }

    public Optional<AnalyticsInfo> getById(UserInfo userInfo, String id) throws DataAccessException {
        // FIXME handle this scenario, should we call short-url-generation service to ask for this detail?
//        if (!redirection.get().uid().equals(userInfo.uid())) {
//            throw new UnauthorizedAccessException();
//        }

        return dao.getById(id).or(() -> Optional.of(new AnalyticsInfo()));
    }

    public void onRedirectionCreated(String id) {
        LOG.debug("Creating analytics info for id:{}", id);
        dao.save(new AnalyticsInfo(id));
    }
}
