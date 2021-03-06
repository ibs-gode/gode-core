package org.ibs.cds.gode.entity.query.parse;

import org.apache.commons.lang3.tuple.Pair;
import org.ibs.cds.gode.entity.query.QueryType;
import org.ibs.cds.gode.entity.query.model.Order;
import org.ibs.cds.gode.entity.query.model.QueryConfig;
import org.ibs.cds.gode.entity.query.model.Select;
import org.ibs.cds.gode.pagination.PageContext;
import org.ibs.cds.gode.pagination.Sortable;

import java.util.Map;
import java.util.Set;

public class JPAQueryParser<E> implements QueryParser<E,JPAQuery<E>>{

    @Override
    public Pair<JPAQuery<E>, PageContext> parse(QueryConfig<E> config, Map<String, Class> fieldMetadata) {
        Select select = config.getSelect();
        PageContext context = PageContext.of(config.getPageNo(), config.getPageSize());
        parseOrderClause(select.getOrder(), context);
        return Pair.of(new JPAQuery(select.getWhere(), select.getOnly(), fieldMetadata), context);
    }

    private void parseOrderClause(Order order, PageContext context){
        if(order == null){
            context.setSortOrder(Set.of(Sortable.by(Sortable.Type.ASC,"createdOn")));
            return;
        }
        String by = order.getBy() == null ? "createdOn" : order.getBy();
        Sortable.Type type = order.getIn() == null ? Sortable.Type.ASC : order.getIn();
        context.setSortOrder(Set.of(Sortable.by(type, by)));
    }

    @Override
    public QueryType getType() {
        return QueryType.JPA;
    }
}
