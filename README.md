# org.openvoxproject/kitchensink

A library of utility functions that are common to several Puppet Labs
clojure projects.

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](https://img.shields.io/clojars/v/org.openvoxproject/kitchensink.svg)](https://clojars.org/org.openvoxproject/kitchensink)

## Using Our Test Utils

Kitchensink provides [utility code](./test/puppetlabs/kitchensink/) for use in tests.
The code is available in a separate "test" jar that you may depend on by using a classifier in your project dependencies.

```clojure
  (defproject yourproject "1.0.0"
    ...
    :profiles {:test {:dependencies [[org.openvoxproject/kitchensink "x.y.z" :classifier "test"]]}})
```

## License

Copyright © 2013 Puppet Labs
Copyright © 2025 Vox Pupuli

Distributed under the [Apache License, version 2](http://www.apache.org/licenses/).

## Support

Please log tickets and issues at our [GitHub issues tracker](https://github.com/OpenVoxProject/clj-kitchensink/issues).
