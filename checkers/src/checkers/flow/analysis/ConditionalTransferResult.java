package checkers.flow.analysis;

import java.util.Map;

import javax.lang.model.type.TypeMirror;

/**
 * Implementation of a {@link TransferResult} with two non-exceptional store;
 * one for the 'then' edge and one for 'else'. The result of
 * {@code getRegularStore} will be the least upper bound of the two underlying
 * stores.
 * 
 * @author Stefan Heule
 * 
 * @param <S>
 *            The {@link Store} used to keep track of intermediate results.
 */
public class ConditionalTransferResult<A extends AbstractValue<A>, S extends Store<S>>
        extends TransferResult<A, S> {

    /** The 'then' result store. */
    protected S thenStore;

    /** The 'else' result store. */
    protected S elseStore;

    /**
     * Create a {@code ConditionalTransferResult} with {@code thenStore} as the
     * resulting store if the corresponding {@link Node} evaluates to
     * {@code true} and {@code elseStore} otherwise.
     * 
     * <p>
     * 
     * <em>Exceptions</em>: If the corresponding {@link Node} throws an
     * exception, then it is assumed that no special handling is necessary and
     * the store before the corresponding {@link Node} will be passed along any
     * exceptional edge.
     * 
     * <p>
     * 
     * <em>Aliasing</em>: {@code thenStore} and {@code elseStore} are not
     * allowed to be used anywhere outside of this class (including use through
     * aliases). Complete control over the objects is transfered to this class.
     */
    public ConditionalTransferResult(A value, S thenStore, S elseStore) {
        super(value);
        this.thenStore = thenStore;
        this.elseStore = elseStore;
    }

    /**
     * Create a {@code ConditionalTransferResult} with {@code thenStore} as the
     * resulting store if the corresponding {@link Node} evaluates to
     * {@code true} and {@code elseStore} otherwise.
     * 
     * <p>
     * 
     * <em>Exceptions</em>: If the corresponding {@link Node} throws an
     * exception, then the corresponding store in {@code exceptionalStores} is
     * used. If no exception is found in {@code exceptionalStores}, then it is
     * assumed that no special handling is necessary and the store before the
     * corresponding {@link Node} will be passed along any exceptional edge.
     * 
     * <p>
     * 
     * <em>Aliasing</em>: {@code thenStore}, {@code elseStore}, and any store in
     * {@code exceptionalStores} are not allowed to be used anywhere outside of
     * this class (including use through aliases). Complete control over the
     * objects is transfered to this class.
     */
    public ConditionalTransferResult(A value, S thenStore, S elseStore,
            Map<TypeMirror, S> exceptionalStores) {
        super(value);
        this.exceptionalStores = exceptionalStores;
        this.thenStore = thenStore;
        this.elseStore = elseStore;
    }

    @Override
    public S getRegularStore() {
        return thenStore.leastUpperBound(elseStore);
    }

    @Override
    public S getThenStore() {
        return thenStore;
    }

    @Override
    public S getElseStore() {
        return elseStore;
    }

    @Override
    public boolean containsTwoStores() {
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("RegularTransferResult(");
        result.append(System.getProperty("line.separator"));
        result.append("resultValue = " + resultValue);
        result.append(System.getProperty("line.separator"));
        result.append("thenStore = " + thenStore);
        result.append("elseStore = " + elseStore);
        result.append(System.getProperty("line.separator"));
        result.append(")");
        return result.toString();
    }

}
