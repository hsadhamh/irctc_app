package irctc.factor.app.irctcmadeeasy.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import irctc.factor.app.irctcmadeeasy.database.PnrInfo;
import irctc.factor.app.irctcmadeeasy.database.TransactionInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;

import irctc.factor.app.irctcmadeeasy.database.PnrInfoDao;
import irctc.factor.app.irctcmadeeasy.database.TransactionInfoDao;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig pnrInfoDaoConfig;
    private final DaoConfig transactionInfoDaoConfig;
    private final DaoConfig passengerInfoDaoConfig;
    private final DaoConfig ticketDetailsDaoConfig;

    private final PnrInfoDao pnrInfoDao;
    private final TransactionInfoDao transactionInfoDao;
    private final PassengerInfoDao passengerInfoDao;
    private final TicketDetailsDao ticketDetailsDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        pnrInfoDaoConfig = daoConfigMap.get(PnrInfoDao.class).clone();
        pnrInfoDaoConfig.initIdentityScope(type);

        transactionInfoDaoConfig = daoConfigMap.get(TransactionInfoDao.class).clone();
        transactionInfoDaoConfig.initIdentityScope(type);

        passengerInfoDaoConfig = daoConfigMap.get(PassengerInfoDao.class).clone();
        passengerInfoDaoConfig.initIdentityScope(type);

        ticketDetailsDaoConfig = daoConfigMap.get(TicketDetailsDao.class).clone();
        ticketDetailsDaoConfig.initIdentityScope(type);

        pnrInfoDao = new PnrInfoDao(pnrInfoDaoConfig, this);
        transactionInfoDao = new TransactionInfoDao(transactionInfoDaoConfig, this);
        passengerInfoDao = new PassengerInfoDao(passengerInfoDaoConfig, this);
        ticketDetailsDao = new TicketDetailsDao(ticketDetailsDaoConfig, this);

        registerDao(TicketDetails.class, ticketDetailsDao);
        registerDao(PnrInfo.class, pnrInfoDao);
        registerDao(TransactionInfo.class, transactionInfoDao);
        registerDao(PassengerInfo.class, passengerInfoDao);
    }
    
    public void clear() {
        pnrInfoDaoConfig.getIdentityScope().clear();
        transactionInfoDaoConfig.getIdentityScope().clear();
        passengerInfoDaoConfig.getIdentityScope().clear();
        ticketDetailsDaoConfig.getIdentityScope().clear();
    }

    public PnrInfoDao getPnrInfoDao() {
        return pnrInfoDao;
    }

    public TransactionInfoDao getTransactionInfoDao() {
        return transactionInfoDao;
    }

    public PassengerInfoDao getPassengerInfoDao() {
        return passengerInfoDao;
    }

    public TicketDetailsDao getTicketDetailsDao() {
        return ticketDetailsDao;
    }

}
