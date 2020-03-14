package ${packageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ${daoInfoPackage};
import ${poInfoPackage};

@Service
public class ${serviceInfoName} extends AbstractBaseService<${poInfoName}, ${idColumnType}> {

    @Autowired
    private ${daoInfoName} dao;

    @Override
    protected ${daoInfoName} getDao() {
        return dao;
    }

}