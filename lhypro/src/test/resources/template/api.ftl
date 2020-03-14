package ${packageName};


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import ${poInfoPackage};
import ${serverInfoPackage};
import com.mapleleaf.management.web.controller.AbstractRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ${controllerInfoName} extends AbstractRestController<${poInfoName}, ${idColumnType}> {

    @Autowired
    private ${serverInfoName} service;

    @Override
    protected ${serverInfoName} getService() {
        return service;
    }


    @GetMapping("/${listUrl}/pager")
    public ResponseEntity<?> findPager(@RequestParam("page") int page,
                                       @RequestParam("limit") int limit,
                                       @RequestParam(required = false) String sort) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        return createOKResponse(getService().findPage(pageBounds));
    }
}
