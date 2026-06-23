use std::sync::Arc;

use crate::error::NovaxError;
use super::{SdkRequest, SdkResponse};
use super::transport::Transport;

pub trait Interceptor: Send + Sync {
    fn intercept(&self, request: SdkRequest, chain: &dyn Chain) -> Result<SdkResponse, NovaxError>;
}

pub trait Chain: Send + Sync {
    fn proceed(&self, request: SdkRequest) -> Result<SdkResponse, NovaxError>;
}

pub(crate) struct ChainNode {
    interceptors: Arc<Vec<Arc<dyn Interceptor>>>,
    transport:    Arc<dyn Transport>,
    index:        usize,
}

impl ChainNode {
    pub fn new(interceptors: Arc<Vec<Arc<dyn Interceptor>>>, transport: Arc<dyn Transport>) -> Self {
        Self { interceptors, transport, index: 0 }
    }
}

impl Chain for ChainNode {
    fn proceed(&self, request: SdkRequest) -> Result<SdkResponse, NovaxError> {
        if self.index < self.interceptors.len() {
            let next = ChainNode {
                interceptors: self.interceptors.clone(),
                transport:    self.transport.clone(),
                index:        self.index + 1,
            };
            self.interceptors[self.index].intercept(request, &next)
        } else {
            self.transport.execute(request)
        }
    }
}
