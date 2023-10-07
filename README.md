For resolve this task I saw to option for implementation so called
1. Optimistic lock solution
2. Pessimistic lock solution

## Optimistic lock
We add for account object version property and do not do update in case when version was incremented
## Pessimistic lock
Add to the Account object ReadWriteLock property and do the lock when we do any manipulation. Or even use synchronisation when on the obtained object from map(this way more complex since object can be just changed)

# Decision
I decided to do it in optimistic lock manner(I supposed it will be more elegant way), but I faced an issue that could not be resolved so probably Pessimistic lock it is the only way. 

P.S. Race condition described in comment for TransferService implementation

P.S Due to lack of time I can not rewrite it. Thanks for understanding.


