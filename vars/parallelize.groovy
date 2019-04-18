/**
 * Parallelizes @param task (Closure) across each Map of arguments in @param argsSets.
 *
 * @param argMaps - List of Maps that will be passed as arguments. If the Map has a 'name' field, it will be used to define the name of the branch.
 * @param task    - Closure that runs the parallelized tasks. Expects a single argument, which is a Map of arguments specified in the argMaps parameter above.
 */
def call(List argMaps, Closure task) {
    Map argBoundTasks = [:]
    Closure bindArgsToTask = { Map args -> { -> task(args) } }

    argMaps.eachWithIndex {
        Map args, Integer index ->
        argBoundTasks[args.name ?: index] = bindArgsToTask(args)
    }

    parallel(argBoundTasks)
}
