package com.example.dict_en_vn.db.dao;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;


/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig customerDaoConfig;
    private final DaoConfig storyDaoConfig;
    private final DaoConfig vn_enDaoConfig;
    private final NoteDao noteDao;
    private final VN_ENDao vn_ENDao;
    private final FamilyDao customerDao;
    private final StoryDao storyDao;
    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        vn_enDaoConfig = daoConfigMap.get(VN_ENDao.class).clone();
        vn_enDaoConfig.initIdentityScope(type);
        
        customerDaoConfig = daoConfigMap.get(FamilyDao.class).clone();
        customerDaoConfig.initIdentityScope(type);
        storyDaoConfig = daoConfigMap.get(FamilyDao.class).clone();
        storyDaoConfig.initIdentityScope(type);
        
        noteDao = new NoteDao(noteDaoConfig, this);
        vn_ENDao = new VN_ENDao(vn_enDaoConfig, this);
        customerDao = new FamilyDao(customerDaoConfig, this);
        storyDao = new StoryDao(customerDaoConfig, this);
        
        registerDao(Note.class, noteDao);
        registerDao(Note.class, vn_ENDao);
        registerDao(Family.class, customerDao);
        registerDao(Story.class, storyDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
        customerDaoConfig.getIdentityScope().clear();
        storyDaoConfig.getIdentityScope().clear();
    }
    public FamilyDao getCustomerDao() {
        return customerDao;
    }

    public NoteDao getEN_VNDao() {
        return noteDao;
    }
    public VN_ENDao getVN_ENDao() {
    	return vn_ENDao;
    }
    public StoryDao getStoryDao() {
    	return storyDao;
    }


}
